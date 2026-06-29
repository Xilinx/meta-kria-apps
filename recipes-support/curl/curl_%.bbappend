# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# Enable curl's TFTP backend. The oe-core curl recipe ships a [tftp]
# PACKAGECONFIG but leaves it out of the default set, so the stock curl
# is built with --disable-tftp. The KR260 TSN multi-node-communication
# app deployment flow uploads per-node Qbv schedules with
# `curl -T <file> tftp://<peer>`, which needs the TFTP protocol compiled
# in. Verify on-target with `curl --version` (Protocols line lists tftp).
PACKAGECONFIG:append = " tftp"
