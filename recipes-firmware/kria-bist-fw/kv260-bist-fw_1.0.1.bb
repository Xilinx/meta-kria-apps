# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria KV260 Built-In Self-Test (BIST) firmware (DT overlay + bitstream)"

require kria-bist-fw.inc

FW_NAME = "kv260-bist"

SRC_URI[sha256sum] = "670615a6c414d19b71bf481239d420866c2b6fecac81fc33f534cb7e1f6481be"

# Out-of-tree AR1335 sensor driver and in-tree =m modules referenced by
# the kv260-bist DT overlay (see recipes-firmware/module-dependencies.md).
RDEPENDS:${PN} += " \
    ar1335-module \
    kernel-module-ap1302 \
    kernel-module-xlnx-vcu \
"

# Modules to load for the kv260-bist overlay. ap1302 (the on-board ISP)
# must be loaded before ar1335 so the ar1335 sensor binding finds the
# upstream ISP. xlnx_vcu drives the VCU encode/decode IP. Other video
# drivers (csi2rxss, demosaic, vpss-scaler, frmbuf, i2c-mux-pca954x,
# i2c-xiic, gpio-xilinx, imx219) are =y in the kernel.
KRIA_FW_MODULES = "ap1302 ar1335 xlnx_vcu"
