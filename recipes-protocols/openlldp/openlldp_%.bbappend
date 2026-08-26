# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# The in-tree meta-networking openlldp tracks intel/openlldp branch-1.1
# (SRCREV f1dd9eb), which lacks the 802.3br Additional Ethernet
# Capabilities TLV (Frame Preemption / MAC Merge). The kr260
# ros2_multinode_communication_via_tsn app deployment flow drives
# `lldpad` / `lldptool -i <if> set -tlv -V addEthCaps ...`, which needs
# that TLV.
#
# The full addEthCaps / MAC-merge / frame-preemption series is upstream on
# intel/openlldp master (NOT on branch-1.1, and NOT on the v1.1.1 tag), so
# repoint the fetch at master. A tag cannot be used here: the newest tag
# (v1.1.1) predates the TLV series.
#
# The only remaining Xilinx-specific delta is the SIOCDEVPRIVATE+15 offset
# for the staging TSN NIC is_bridge() ioctl, carried here as a single
# file:// patch. Both the master repin and the patch are expected to drop
# once a lldpad recipe lands in meta-xilinx-tsn (and once the mainline TSN
# driver removes the need for the +15 offset).
#
# The base recipe's 0004-clif-Include-string.h patch is already applied
# upstream on master, so drop it from SRC_URI; the other base patches
# (musl, autotools) still apply. COPYING is unchanged, so LIC_FILES_CHKSUM
# stays valid.

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:remove = "git://github.com/intel/openlldp.git;protocol=https;branch=branch-1.1"
SRC_URI:remove = "file://0004-clif-Include-string.h-for-mem-function-prototypes.patch"
SRC_URI:prepend = "git://github.com/intel/openlldp.git;protocol=https;branch=master \
                   file://0001-lldp_util-use-SIOCDEVPRIVATE-15-for-Xilinx-TSN-NIC.patch "
SRCREV = "ad0aaf147f63a5ff585c8bfbebe77871cf12e0e6"
