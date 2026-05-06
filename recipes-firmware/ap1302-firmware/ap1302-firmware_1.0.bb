# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "ON Semiconductor AP1302 ISP firmware blobs"
DESCRIPTION = "Pre-built AP1302 ISP firmware binaries (ar0144 and ar1335 \
single-sensor variants) loaded by the in-tree ap1302 V4L2 subdev driver \
via request_firmware() at probe time. Required for the Kria KV260 BIST \
camera pipeline."
HOMEPAGE = "https://github.com/Xilinx/ap1302-firmware"
SECTION = "firmware"

# Proprietary onsemi/AP1302 firmware license. The text shipped in the
# upstream repo (https://github.com/Xilinx/ap1302-firmware) is the
# binding agreement, not a recognised OSI license. Declare it as a
# custom firmware license so do_populate_lic stages the agreement text
# alongside the binary; no LICENSE_FLAGS gating is added because the
# upstream Xilinx repo already redistributes these blobs publicly,
# matching how meta-* layers ship comparable linux-firmware blobs.
LICENSE = "Firmware-AP1302"
NO_GENERIC_LICENSE[Firmware-AP1302] = "LICENSE.txt"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9c13aad1aab42f76326f1beceafc40c4"

SRC_URI = "git://github.com/Xilinx/ap1302-firmware.git;protocol=https;branch=main"
# v0 of the repo - "enable license check via github actions". The two .bin
# files are the source of truth and have not changed since the repo's
# initial publish.
SRCREV = "eafedf17d971b1bddb37e49577c7a4ba0ec6f49e"
PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"

inherit allarch

# ap1302 driver calls request_firmware() with names of the form
# "ap1302_<sensor>_single_fw.bin" / "_dual_fw.bin", which the kernel
# firmware loader resolves under ${nonarch_base_libdir}/firmware (i.e.
# /lib/firmware on the rootfs and inside the OCI containers). Install
# the blobs flat at the firmware root so the driver finds them without
# any subdir search.
do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware
    install -m 0644 ${S}/ap1302_ar0144_single_fw.bin \
        ${D}${nonarch_base_libdir}/firmware/ap1302_ar0144_single_fw.bin
    install -m 0644 ${S}/ap1302_ar1335_single_fw.bin \
        ${D}${nonarch_base_libdir}/firmware/ap1302_ar1335_single_fw.bin
}

FILES:${PN} = " \
    ${nonarch_base_libdir}/firmware/ap1302_ar0144_single_fw.bin \
    ${nonarch_base_libdir}/firmware/ap1302_ar1335_single_fw.bin \
"
