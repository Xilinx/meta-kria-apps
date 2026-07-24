# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria TSN + RS-485 multi-node communication / test OCI container image"
DESCRIPTION = "OCI container image for the ROS 2 \
multi-node-communication-via-TSN application and the TSN / RS-485 test \
suite. Both the KR260 kr260-tsn-rs485pmod design and the KD240 \
kd240-motor-ctrl-qei design carry the TSN IP (and the RS-485 PMOD), so \
the same image serves either board: it bundles both firmware overlays, \
the AMD Xilinx TSN runtime (gPTP / linuxptp / lldpd / tsn-utils / \
tsn-examples via packagegroup-tsn), the pmod-rs485-test Modbus tool, \
the ros-tsn-pubsub talker/listener ROS 2 example, and the helper tools \
the deployment flow drives: tsn-networkmanager \
watches a config drop dir with inotify-tools and pushes per-node Qbv \
schedules with curl over TFTP to the peer node's tftp-hpa-server. \
python3-libconf parses the libconfig schedule files."

require recipes-containers/kria-app-container.inc

# Leave the OCI entrypoint empty so the operator launches the TSN
# manager, an example, or a shell explicitly. See
# container-foc-motor-ctrl-server.bb for the rationale.
CONTAINER_SHELL = "bash"
CONTAINER_APP_CMD = ""

# Runtime invocation - two profiles, same image:
#
# 1) Endpoint / node role (KR260/KD240 board that binds the PL overlay,
#    runs the TSN endpoint/switch tooling and the receiving tftpd). Needs
#    the PL, kernel resources, dfx-mgr and register access:
#
#      sudo docker run --rm -it --privileged \
#          --net=host \
#          -v /dev:/dev \
#          -v /sys:/sys \
#          -v /var/run/dfx-mgrd.socket:/var/run/dfx-mgrd.socket \
#          container-tsn-rs485pmod:latest /bin/bash
#
#    Do NOT mount /run or /etc here: mounting /run makes the container
#    see the host's /run/systemd/system and take the systemctl tftpd
#    path (no systemd in the container), and mounting /etc shadows the
#    container's /etc/default/tftpd-hpa. Both re-break the standalone
#    tftpd start.
#
# 2) CNC / network-manager role (the node that generates the schedules
#    and pushes them out). This is a pure TFTP *client* (xcnc_setup ->
#    qbv.py/fdb.py -> tftp_nm.py `curl -T tftp://<node>`); it never binds
#    the PL, so it needs neither --privileged, /dev, /sys, nor the
#    dfx-mgr socket. Only host networking is required so arp can resolve
#    node MAC->IP and the UDP TFTP traffic reaches the boards:
#
#      sudo docker run --rm -it --net=host \
#          container-tsn-rs485pmod:latest /bin/bash
#
#    The same image serves both roles. On a non-Kria Linux CNC host, the
#    documented git-clone method is used instead of this container.

# Runtime deps that really belong in the upstream tsn-utils recipe's
# RDEPENDS (or in packagegroup-tsn) in meta-xilinx-tsn, but are not
# declared there yet. The tsn-networkmanager / tsn-scripts programs
# shell out to and import all of these, so we carry them here to make
# the container self-contained. This is INTENTIONALLY REDUNDANT with
# the eventual upstream RDEPENDS - keep it until meta-xilinx-tsn covers
# them, then delete this whole variable. Per-package justification:
#   inotify-tools  - inotifywait, config-dir watch
#   python3-libconf- `import libconf` in qbv-parser.py (separate recipe,
#                    not part of python3-modules)
#   python3-pyyaml - `import yaml` in fdb.py (separate recipe, not part
#                    of python3-modules)
#   python3-modules- pulls the full CPython stdlib. The network-manager
#                    scripts import a scattered set of stdlib modules
#                    that Yocto splits into many python3-* subpackages;
#                    rather than track each one (brittle when upstream
#                    adds an import), pull them all. It grows the image
#                    by ~13 MB compressed (bz2 ~32M -> ~45M); the
#                    robustness is worth it here. The specific stdlib
#                    modules actually imported (for reference / if a
#                    minimal set is ever wanted):
#                      json       -> python3-json    (xcnc_setup, fdb.py,
#                                    qbv.py, common.py)
#                      string     -> python3-stringold (qbv.py, fdb.py)
#                      datetime   -> python3-datetime (common.py,
#                                    qbv-parser.py)
#                      decimal    -> python3-numbers  (qbv-parser.py)
#                      pty        -> python3-terminal (qbv-parser.py)
#                      shlex      -> python3-shell    (qbv-parser.py)
#                      re/os/sys/io/subprocess/math/signal/array/time/
#                      shutil     -> python3-core (always present)
#   curl           - TFTP schedule upload in tftp_nm.py; needs the tftp
#                    PACKAGECONFIG from our curl bbappend
#   tftp-hpa-server- in.tftpd-hpa + /etc/default/tftpd-hpa (receive side)
#   busybox        - tftp_nm.py probes busybox for a tftp applet; also
#                    supplies the devmem applet (our busybox bbappend)
#   freeipmi       - ipmi-fru MAC/FRU read in mac_setup; RDEPENDed by the
#                    dfx-mgr daemon but NOT dfx-mgr-client (what we install)
REDUNDANT_TSNUTILS_RDEPENDS = " \
    inotify-tools \
    python3-libconf \
    python3-pyyaml \
    python3-modules \
    curl \
    tftp-hpa-server \
    busybox \
    freeipmi \
"

# Firmware overlays for both TSN-capable designs. The TSN IP (and the
# RS-485 PMOD) is present on both the KR260 kr260-tsn-rs485pmod design
# and the KD240 kd240-motor-ctrl-qei design, so the TSN + RS-485 test
# suite runs on either board and this one image serves both. Each *-fw
# recipe pulls its own kernel-module set transitively
# (uio_pdrv_genirq, mcp251x, motor-control-iio-modules, amd-axi-w1,
# etc.), so listing the firmware names is enough.
#
# pmod-rs485-test - libmodbus RS-485 loopback / sensor self-test.
#
# packagegroup-tsn pulls the full TSN runtime (tsn-utils + tsn-examples
# + gptp/linuxptp/lldpd). tcpdump is a container-side capture tool for
# TSN traffic (not a tsn-utils dep, so it stays here rather than in
# REDUNDANT_TSNUTILS_RDEPENDS).
#
# ethtool - link/feature introspection used by the TSN bring-up flow.
#
# openlldp - provides lldpad/lldptool. The app_deployment flow uses the
# 802.3br Additional Ethernet Capabilities TLV (lldptool ... set -tlv -V
# addEthCaps), which the stock meta-networking openlldp (branch-1.1)
# does NOT carry. This currently REQUIRES the meta-local-overrides
# openlldp bbappend, which repoints the recipe at the Xilinx TSN branch.
# Without that override a build gets stock openlldp and the addEthCaps
# TLV will be missing. The TSN openlldp/lldpad should move into
# meta-xilinx-tsn so this no longer depends on the override.
#
# ros-tsn-pubsub - the xlnx-pubsub ROS 2 talker/listener example the TSN
# demo streams over the Qbv-scheduled link; also pulls the ros2 CLI run
# verb it is launched with.
#
# can-utils - cansend / candump / cangen for the CAN interface test. Both
# the kr260-tsn-rs485pmod and kd240-motor-ctrl-qei designs expose a CAN
# link; the CAN self-test drives it with cansend, which is not pulled by
# packagegroup-tsn.
CONTAINER_APP = " \
    kr260-tsn-rs485pmod-fw \
    kd240-motor-ctrl-qei-fw \
    packagegroup-tsn \
    pmod-rs485-test \
    ros-tsn-pubsub \
    tcpdump \
    ethtool \
    openlldp \
    can-utils \
    ${REDUNDANT_TSNUTILS_RDEPENDS} \
"
