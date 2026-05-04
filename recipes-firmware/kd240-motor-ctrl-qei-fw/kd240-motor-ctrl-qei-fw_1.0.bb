# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria KD240 FOC motor-control (QEI) firmware (DT overlay + bitstream)"
HOMEPAGE = "https://github.com/Xilinx/kria-vitis-platforms"
SECTION = "firmware"
LICENSE = "MIT"
# S points inside the unpacked release tarball, but the bundled MIT text is
# fetched into ${WORKDIR} via file://LICENSE; reach it via ../.
LIC_FILES_CHKSUM = "file://../LICENSE;md5=57d17a0caea0d565f8f673971e3c2e10"

FW_NAME = "kd240-motor-ctrl-qei"
FW_VERSION = "v1.0"
FW_BASE_URL = "https://github.com/Xilinx/kria-vitis-platforms/releases/download/${FW_VERSION}"

# The release tarball does not ship a LICENSE file, so attach the MIT text
# from this layer for LIC_FILES_CHKSUM and license manifesting.
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI = "${FW_BASE_URL}/${FW_NAME}.tar.gz \
           file://LICENSE"
SRC_URI[sha256sum] = "d0a7cfcc3a75dc4d1846f0d358eae4fcd8b2a24e1b883d9cf13bbd5f50e6a48a"

S = "${WORKDIR}/${FW_NAME}"

inherit allarch

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/xilinx/${FW_NAME}
    cp -r ${S}/lib/firmware/xilinx/${FW_NAME}/. \
        ${D}${nonarch_base_libdir}/firmware/xilinx/${FW_NAME}/
}

FILES:${PN} = "${nonarch_base_libdir}/firmware/xilinx/${FW_NAME}"

# Out-of-tree IIO drivers and in-tree =m modules referenced by the
# kd240-motor-ctrl-qei DT overlay (see recipes-firmware/module-dependencies.md).
RDEPENDS:${PN} += " \
    motor-control-iio-modules \
    kernel-module-uio-pdrv-genirq \
    kernel-module-amd-axi-w1 \
"

# Modules to load for the kd240-motor-ctrl-qei overlay. uio_pdrv_genirq
# must come first so generic-uio nodes can bind. amd_axi_w1 drives the
# AXI 1-Wire master IP. The TSN driver set is built into the kernel
# (CONFIG_XILINX_TSN=y) and does not need explicit loading.
KRIA_FW_MODULES = "uio_pdrv_genirq amd_axi_w1 xilinx-adc-hub hls-foc-periodic hls-pwm-gen hls-qei-axi hls-svpwm-duty"

require ../kria-fw-modules-load.inc
