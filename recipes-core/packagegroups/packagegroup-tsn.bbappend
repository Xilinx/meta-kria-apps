# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# TSN bring-up tools used by the ros2_multinode_communication_via_tsn app
# deployment flow that upstream packagegroup-tsn (meta-xilinx-tsn) does not
# yet pull. These belong in packagegroup-tsn upstream - carry here until it
# covers them, then drop this bbappend.
#   ethtool  - link/feature introspection and MAC Merge (--show-mm/--set-mm)
#              used throughout TSN bring-up.
#   openlldp - lldpad / lldptool for the 802.3br Additional Ethernet
#              Capabilities TLV (frame preemption / MAC Merge enablement).
#
# Appended via RDEPENDS:${PN} (not TSN_PACKAGES): the base recipe assigns
# RDEPENDS:${PN} = "${TSN_PACKAGES}" at parse time, so appending to the
# variable afterward would have no effect.
RDEPENDS:${PN} += " \
    ethtool \
    openlldp \
"
