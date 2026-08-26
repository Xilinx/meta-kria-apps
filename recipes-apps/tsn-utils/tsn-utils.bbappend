# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# Temporary SRCREV bump for the EDF 2026.1 container build. The
# meta-xilinx-tsn tsn-utils recipe on rel-v2026.1 is frozen at an older
# SRCREV (31eda80) that predates the EDF-specific fixes merged upstream.
# Repin to the current github.com/Xilinx/tsn-utils main, which now carries
# them all. Drop this bbappend once the meta-xilinx-tsn tsn-utils SRCREV is
# bumped past 1db91b0.
#
# Fixes now included on main (no file:// patches needed):
#   - Drop xmutil from the tsn-scripts / network-manager flow: overlay-slot
#     extraction uses `dfx-mgr-client -listPackage`, and mac_setup.sh reads
#     MAC IDs from the FRU EEPROM via `ipmi-fru` (needs freeipmi at runtime).
#   - Make the tsn-networkmanager tftpd start/status service-manager
#     agnostic (works without systemd, e.g. inside the container).
#   - pcp_prog auto-detects the TSN module name for its sysfs parameter
#     path (the endpoint driver folded into the composite xilinx_emac_tsn
#     module, so the old hard-coded xilinx_tsn_ep path was gone).
#   (upstream refs: tsn-utils PR #32 + #33.)
#
# No release tag tracks this: the xilinx_v2026.1 tag predates these fixes,
# so pin the merged main commit.

SRCREV = "1db91b04d98927a05f868e6365899b13657e76d5"

# Runtime dependencies the tsn-networkmanager / tsn-scripts programs shell
# out to or import at runtime. Upstream tsn-utils declares only
# `python3-libconf bash`; these are the rest. Belongs in the upstream
# tsn-utils recipe RDEPENDS - carry here until meta-xilinx-tsn covers it.
#   inotify-tools   - inotifywait, config-dir watch (tsn-networkmanager)
#   python3-pyyaml  - `import yaml` in fdb.py
#   python3-modules - the network-manager scripts import a scattered set of
#                     stdlib modules (json, datetime, decimal, pty, shlex,
#                     string, ...) that Yocto splits into many python3-*
#                     subpackages; pull them all rather than track each.
#   curl            - TFTP schedule upload in tftp_nm.py (`curl -T tftp://`).
#                     NOTE: also needs the `tftp` PACKAGECONFIG, enabled by
#                     the curl_%.bbappend in this layer - RDEPENDS alone does
#                     not turn that on.
#   tftp-hpa-server - in.tftpd-hpa + /etc/default/tftpd-hpa (receive side)
#   busybox         - tftp_nm.py aborts unless `busybox` reports a tftp
#                     applet (it then transfers via curl). The tftp applet is
#                     already on in the poky busybox defconfig, so only the
#                     package needs to be present. NOTE: this layer's
#                     busybox_%.bbappend enables the devmem applet (a separate
#                     need); it does NOT touch tftp.
#   freeipmi        - `ipmi-fru` MAC/FRU read in mac_setup (RDEPENDed by the
#                     dfx-mgr daemon but not dfx-mgr-client, which we install)
RDEPENDS:${PN} += " \
    inotify-tools \
    python3-pyyaml \
    python3-modules \
    curl \
    tftp-hpa-server \
    busybox \
    freeipmi \
"
