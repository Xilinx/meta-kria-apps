# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# Common bits shared by every Kria application OCI container in this
# layer. Pulls in the minimum userspace each container is expected to
# carry (coreutils-style helpers, dfx-mgr-client for loading firmware
# overlays from inside the container, and iproute2 for ip / link
# configuration).

require recipes-extended/images/container-app-base.bb

# Base userspace every Kria app container is expected to carry. Use
# IMAGE_INSTALL because container-base.bb sets NO_RECOMMENDATIONS=1, so
# RRECOMMENDS would silently be dropped.
KRIA_CONTAINER_BASE_INSTALL = " \
    packagegroup-core-base-utils \
    dfx-mgr-client \
    iproute2 \
    dfx-fw-install \
    kria-modules-load \
"
IMAGE_INSTALL:append = " ${KRIA_CONTAINER_BASE_INSTALL}"
