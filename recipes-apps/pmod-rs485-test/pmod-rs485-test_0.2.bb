# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria PMOD RS-485 loopback / Modbus self-test"
DESCRIPTION = "Small C utility used to exercise the RS-485 PMOD on Kria \
KR260 via libmodbus. Installed under /opt/xilinx/pmod-rs485-test/bin."
HOMEPAGE = "https://github.com/Xilinx/pmod-rs485-test"
SECTION = "tests"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5c9f774c9491b577f954e84fc7ec0007"

SRC_URI = "git://github.com/Xilinx/pmod-rs485-test.git;branch=main;protocol=https"
SRCREV = "2870017d89a0db122b78f148be0174318437e0d0"

S = "${WORKDIR}/git"

DEPENDS = "libmodbus"
RDEPENDS:${PN} = "libmodbus"

# Upstream Makefile uses bare $(CC) and expects modbus.h via -lmodbus's
# pkg-config include dir (/usr/include/modbus). Pass that include path
# explicitly since the Makefile does not call pkg-config itself.
CFLAGS:append = " -I${STAGING_INCDIR}/modbus"

do_compile() {
    oe_runmake
}

PMOD_RS485_INSTALL_DIR = "/opt/xilinx/pmod-rs485-test"

do_install() {
    oe_runmake install DESTDIR=${D} prefix=${PMOD_RS485_INSTALL_DIR}
}

FILES:${PN} = "${PMOD_RS485_INSTALL_DIR}"
