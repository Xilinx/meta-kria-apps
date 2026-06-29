# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# Turn on the busybox devmem applet (off in the poky defconfig) so the
# KR260 TSN container can peek/poke memory-mapped registers per the
# ros2_multinode_communication_via_tsn app deployment flow. This only
# flips a config switch; busybox is pulled into the rootfs only by the
# images that explicitly install it.
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://devmem.cfg"
