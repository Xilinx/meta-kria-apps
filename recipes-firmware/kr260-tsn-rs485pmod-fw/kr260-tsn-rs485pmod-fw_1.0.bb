# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria KR260 TSN + RS485 PMOD firmware (DT overlay + bitstream)"
HOMEPAGE = "https://github.com/Xilinx/kria-vitis-platforms"
SECTION = "firmware"
LICENSE = "MIT"
# S points inside the unpacked release tarball, but the bundled MIT text is
# fetched into ${WORKDIR} via file://LICENSE; reach it via ../.
LIC_FILES_CHKSUM = "file://../LICENSE;md5=57d17a0caea0d565f8f673971e3c2e10"

FW_NAME = "kr260-tsn-rs485pmod"
FW_VERSION = "v1.0"
FW_BASE_URL = "https://github.com/Xilinx/kria-vitis-platforms/releases/download/${FW_VERSION}"

# The release tarball does not ship a LICENSE file, so attach the MIT text
# from this layer for LIC_FILES_CHKSUM and license manifesting.
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI = "${FW_BASE_URL}/${FW_NAME}.tar.gz \
           file://LICENSE"
SRC_URI[sha256sum] = "53a19bab48c5f22d21436eb5ae008f32275b53c731ddfd82a2e2fef9c50b2e21"

S = "${WORKDIR}/${FW_NAME}"

inherit allarch

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/xilinx/${FW_NAME}
    cp -r ${S}/lib/firmware/xilinx/${FW_NAME}/. \
        ${D}${nonarch_base_libdir}/firmware/xilinx/${FW_NAME}/
}

FILES:${PN} = "${nonarch_base_libdir}/firmware/xilinx/${FW_NAME}"
