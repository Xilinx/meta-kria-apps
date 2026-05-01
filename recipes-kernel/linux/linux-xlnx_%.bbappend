# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# Enable a small set of kernel modules required by the firmware overlays
# shipped in this layer. The fragment only flips =m switches; the kernel
# image itself is unchanged at runtime, the rootfs install set is unchanged,
# and the new kernel-module-* split packages are only pulled into the
# OCI container images that explicitly RDEPEND on them.
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://kria-apps-extra.cfg"
