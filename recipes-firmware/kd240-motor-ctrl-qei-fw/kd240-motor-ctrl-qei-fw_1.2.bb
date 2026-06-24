# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria KD240 FOC motor-control (QEI) firmware (DT overlay + bitstream)"
HOMEPAGE = "https://github.com/Xilinx/kria-vitis-platforms"
SECTION = "firmware"
# Released tarball bundles AMD/Xilinx binary-only license at
# <fw-name>/LICENSE; declare it via NO_GENERIC_LICENSE since the license
# text is not part of poky's COMMON_LICENSE_DIR.
LICENSE = "Advanced-Micro-Devices-Xilinx-binaries"
LIC_FILES_CHKSUM = "file://LICENSE;md5=17ed81f78abe63ee929646937685e8d1"
NO_GENERIC_LICENSE[Advanced-Micro-Devices-Xilinx-binaries] = "LICENSE"

FW_NAME = "kd240-motor-ctrl-qei"
FW_VERSION = "v1.2"
FW_BASE_URL = "https://github.com/Xilinx/kria-vitis-platforms/releases/download/${FW_VERSION}"

SRC_URI = "${FW_BASE_URL}/${FW_NAME}.tar.gz"
SRC_URI[sha256sum] = "67f01455f4695eab4626eb1c7fac717225efa454d1a564b3cfc4264808be8da2"

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
    kernel-module-w1-therm \
"

# Modules to load for the kd240-motor-ctrl-qei overlay. uio_pdrv_genirq
# must come first so generic-uio nodes can bind. amd_axi_w1 drives the
# AXI 1-Wire master IP; w1_therm must follow it so the 1-Wire thermal
# slaves bind and expose their hwmon temp inputs. The TSN driver set is
# built into the kernel (CONFIG_XILINX_TSN=y) and does not need loading.
KRIA_FW_MODULES = "uio_pdrv_genirq amd_axi_w1 w1_therm xilinx-adc-hub hls-foc-periodic hls-pwm-gen hls-qei-axi hls-svpwm-duty"

require ../kria-fw-modules-load.inc
