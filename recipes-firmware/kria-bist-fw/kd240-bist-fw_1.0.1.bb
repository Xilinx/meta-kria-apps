# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria KD240 Built-In Self-Test (BIST) firmware (DT overlay + bitstream)"

require kria-bist-fw.inc

FW_NAME = "kd240-bist"

SRC_URI[sha256sum] = "746cb3d6cdd32a5e850094b4304557ba87e3b2046bb6061a347f14803517f678"

# Out-of-tree IIO drivers and in-tree =m modules referenced by the
# kd240-bist DT overlay (see recipes-firmware/module-dependencies.md).
RDEPENDS:${PN} += " \
    motor-control-iio-modules \
    kernel-module-uio-pdrv-genirq \
    kernel-module-mcp251x \
"

# Modules to load for the kd240-bist overlay (matches the dtsi audit in
# recipes-firmware/module-dependencies.md). uio_pdrv_genirq must come
# first so the generic-uio nodes can bind once the overlay is loaded.
KRIA_FW_MODULES = "uio_pdrv_genirq mcp251x xilinx-adc-hub hls-foc-periodic hls-pwm-gen hls-qei-axi hls-svpwm-duty"
