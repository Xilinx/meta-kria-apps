# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# The kria-bist test suite (recipes-apps/kria-bist) imports the libiio
# python binding (`import iio`), but the libiio_0.25 recipe in
# meta-openembedded leaves the libiio-python3 PACKAGECONFIG out of its
# default set. Enable it here so the libiio-python3 split package is
# actually produced and can be pulled into the BIST container.
PACKAGECONFIG:append = " libiio-python3"
