# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria KR260 Built-In Self-Test (BIST) firmware (DT overlay + bitstream)"

require kria-bist-fw.inc

FW_NAME = "kr260-bist"

SRC_URI[sha256sum] = "f0f482bd16466be7e4cb9d8a39ec2b0eda31c80b7c7fb51093e034a4bfeecb09"

# Out-of-tree FRAMOS IMX547 sensor driver referenced by the kr260-bist
# DT overlay (see recipes-firmware/module-dependencies.md).
RDEPENDS:${PN} += "mv-camera-sensor-module"

# Modules to load for the kr260-bist overlay. Only the out-of-tree
# FRAMOS IMX547 sensor driver needs explicit loading; the rest of the
# overlay (axienet, axi-iic, frmbuf, csi2rxss, video pipeline) binds
# against drivers that are built into the kernel (=y in defconfig).
KRIA_FW_MODULES = "imx547"
