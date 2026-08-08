# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria TSN + RS-485 multi-node communication / test OCI container image"
DESCRIPTION = "OCI container image for the ROS 2 \
multi-node-communication-via-TSN application and the TSN / RS-485 test \
suite. Both the KR260 kr260-tsn-rs485pmod design and the KD240 \
kd240-motor-ctrl-qei design carry the TSN IP (and the RS-485 PMOD), so \
the same image serves either board: it bundles both firmware overlays, \
the AMD Xilinx TSN runtime via packagegroup-tsn (gPTP / linuxptp / \
tsn-utils / tsn-examples, plus ethtool and openlldp - lldpad / lldptool \
for the 802.3br frame-preemption / MAC Merge enablement), the \
pmod-rs485-test Modbus tool, and the ros-tsn-pubsub talker/listener ROS 2 \
example. The tsn-utils runtime deps come from the tsn-utils recipe \
RDEPENDS: tsn-networkmanager watches a config drop dir with inotify-tools \
and pushes per-node Qbv schedules with curl over TFTP to the peer node's \
tftp-hpa-server, and python3-libconf parses the libconfig schedule files."

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
# + gptp/linuxptp/lldpd). The tsn-utils script runtime deps are declared
# in this layer's tsn-utils.bbappend (RDEPENDS), and the TSN bring-up
# tools ethtool + openlldp (lldpad/lldptool for the 802.3br addEthCaps /
# MAC Merge TLV) in this layer's packagegroup-tsn.bbappend - so they no
# longer need to be hand-listed here. tcpdump is a container-side capture
# tool for TSN traffic (not a TSN runtime dep), so it stays listed here.
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
    can-utils \
"
