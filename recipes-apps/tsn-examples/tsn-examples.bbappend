# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# Temporary SRCREV bump for the EDF 2026.1 container build. The
# meta-xilinx-tsn tsn-examples recipe on rel-v2026.1 is frozen at an older
# SRCREV (b68dc4e) that predates the EDF-specific tsn-scripts fixes merged
# upstream. Repin to the current github.com/Xilinx/tsn-talker-listener
# main, which now carries them. Drop this bbappend once the meta-xilinx-tsn
# tsn-examples SRCREV is bumped past b55887e.
#
# Fixes now included on main (no file:// patches needed):
#   - tsn-scripts get_st_pcp() reads ST PCP from sysfs with module-name
#     auto-detection. It used to parse the old xilinx_tsn_ep bootarg, which
#     silently misclassified scheduled traffic as Best Effort once the
#     endpoint driver folded into xilinx_emac_tsn.
#   - tsn-scripts hostname check recognises the "amd-edf" EDF hostname.
#   (upstream refs: tsn-talker-listener PR #30 + #31.)
#
# No release tag tracks this: the xilinx_v2026.1 tag predates these fixes,
# so pin the merged main commit.

SRCREV = "b55887eaf939399868c17a2e7f1fb7684a6be980"
