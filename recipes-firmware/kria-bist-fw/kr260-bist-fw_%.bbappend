# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# TEMPORARY workaround: the upstream kr260-bist firmware tarball ships a
# DT overlay that fails to load on KR260 because of a stale gpio node.
# Override SRC_URI with a locally-patched tarball (gpio node removed) so
# the rest of the firmware recipe can be exercised end-to-end while the
# upstream fix is in flight. Drop this bbappend (and the
# files/kr260-bist-fixup/ directory) once the next kria-vitis-platforms
# release ships the corrected blob.
FILESEXTRAPATHS:prepend := "${THISDIR}/files/kr260-bist-fixup:"

SRC_URI = "file://kr260-bist.tar.gz \
           file://LICENSE"
SRC_URI[sha256sum] = "b588049a1981cfe4554cf9b887194faa7da8fbd2566bb1f79d8c33247b7ee492"
