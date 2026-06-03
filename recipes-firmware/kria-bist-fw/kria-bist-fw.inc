# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# Common bits for the per-board Kria BIST firmware recipes. Each board
# recipe (kd240-bist-fw / kr260-bist-fw / kv260-bist-fw) sets FW_NAME and
# the SRC_URI sha256 checksum and requires this file.

HOMEPAGE = "https://github.com/Xilinx/kria-vitis-platforms"
SECTION = "firmware"
LICENSE = "MIT"
# S points inside the unpacked release tarball, but the bundled MIT text is
# fetched into ${WORKDIR} via file://LICENSE; reach it via ../.
LIC_FILES_CHKSUM = "file://../LICENSE;md5=57d17a0caea0d565f8f673971e3c2e10"

FW_VERSION ?= "v1.2"
FW_BASE_URL ?= "https://github.com/Xilinx/kria-vitis-platforms/releases/download/${FW_VERSION}"

# The release tarballs do not ship a LICENSE file, so attach the MIT text
# from this layer for LIC_FILES_CHKSUM and license manifesting.
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI = "${FW_BASE_URL}/${FW_NAME}.tar.gz \
           file://LICENSE"

S = "${WORKDIR}/${FW_NAME}"

inherit allarch

# Tarballs unpack to <FW_NAME>/lib/firmware/xilinx/<FW_NAME>/{shell.json,
# *.bin, *.dtbo}. Mirror that into the rootfs.
do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/xilinx/${FW_NAME}
    cp -r ${S}/lib/firmware/xilinx/${FW_NAME}/. \
        ${D}${nonarch_base_libdir}/firmware/xilinx/${FW_NAME}/
}

FILES:${PN} = "${nonarch_base_libdir}/firmware/xilinx/${FW_NAME}"

# Per-overlay kernel-module load list (consumed by kria-modules-load
# inside the BIST containers, or systemd-modules-load.service on a
# systemd host). Each per-board recipe sets KRIA_FW_MODULES with the
# module basenames its dtbo nodes bind against.
require ../kria-fw-modules-load.inc
