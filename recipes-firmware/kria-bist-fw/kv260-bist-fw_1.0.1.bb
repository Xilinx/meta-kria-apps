# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria KV260 Built-In Self-Test (BIST) firmware (DT overlay + bitstream)"

require kria-bist-fw.inc

FW_NAME = "kv260-bist"

SRC_URI[sha256sum] = "670615a6c414d19b71bf481239d420866c2b6fecac81fc33f534cb7e1f6481be"
