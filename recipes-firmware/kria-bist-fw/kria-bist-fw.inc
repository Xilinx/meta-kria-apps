# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# Common bits for the per-board Kria BIST firmware recipes. Each board
# recipe (kd240-bist-fw / kr260-bist-fw / kv260-bist-fw) sets FW_NAME and
# the SRC_URI sha256 checksum and requires this file.

HOMEPAGE = "https://github.com/Xilinx/kria-vitis-platforms"
SECTION = "firmware"
# The kria-vitis-platforms release tarballs (v1.2+) bundle the AMD/Xilinx
# binary-only redistribution license at <fw-name>/LICENSE, which is the
# right license for the bitstream / dtbo / xclbin payload. The license is
# not in poky's COMMON_LICENSE_DIR, so declare it via NO_GENERIC_LICENSE
# pointing at the same LICENSE file inside the unpacked tarball.
LICENSE = "Advanced-Micro-Devices-Xilinx-binaries"
LIC_FILES_CHKSUM = "file://LICENSE;md5=17ed81f78abe63ee929646937685e8d1"
NO_GENERIC_LICENSE[Advanced-Micro-Devices-Xilinx-binaries] = "LICENSE"

FW_VERSION ?= "v1.2"
FW_BASE_URL ?= "https://github.com/Xilinx/kria-vitis-platforms/releases/download/${FW_VERSION}"

SRC_URI = "${FW_BASE_URL}/${FW_NAME}.tar.gz"

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
