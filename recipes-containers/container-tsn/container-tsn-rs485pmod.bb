# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria KR260 TSN multi-node communication OCI container image"
DESCRIPTION = "OCI container image targeting Kria KR260 for the ROS 2 \
multi-node-communication-via-TSN application. Bundles the \
kr260-tsn-rs485pmod firmware overlay, the AMD Xilinx TSN runtime \
(gPTP / linuxptp / lldpd / tsn-utils / tsn-examples via \
packagegroup-tsn) and the helper tools the deployment flow drives: \
tsn-networkmanager watches a config drop dir with inotify-tools and \
pushes per-node Qbv schedules with curl over TFTP to the peer node's \
tftp-hpa-server. python3-libconf parses the libconfig schedule files."

require recipes-containers/kria-app-container.inc

# Leave the OCI entrypoint empty so the operator launches the TSN
# manager, an example, or a shell explicitly. See
# container-foc-motor-ctrl-server.bb for the rationale.
CONTAINER_SHELL = "bash"
CONTAINER_APP_CMD = ""

# kr260-tsn-rs485pmod-fw pulls its kernel-module set transitively
# (uio_pdrv_genirq, mcp251x). packagegroup-tsn pulls the full TSN
# runtime including tsn-utils (which already RDEPENDS python3-libconf
# and bash) and tsn-examples. The remaining entries are the explicit
# host-side tools the app_deployment flow uses but that are not pulled
# transitively: inotify-tools (config-dir watch), curl (schedule
# upload), tftp-hpa-server (peer-node receive), and python3-libconf
# (listed defensively in case tsn-utils' RDEPENDS changes upstream).
# tcpdump captures TSN traffic on the endpoint; busybox supplies the
# devmem applet (enabled via our busybox bbappend) for register pokes.
# freeipmi provides ipmi-fru, used by the tsn-networkmanager mac_setup
# scripts to read the board's MAC IDs / product name from the FRU EEPROM
# (--interpret-oem-data). It is RDEPENDed by the dfx-mgr daemon package
# but NOT by dfx-mgr-client (which is what this container installs), so
# it must be listed explicitly.
CONTAINER_APP = " \
    kr260-tsn-rs485pmod-fw \
    packagegroup-tsn \
    inotify-tools \
    python3-libconf \
    curl \
    tftp-hpa-server \
    tcpdump \
    busybox \
    freeipmi \
"
