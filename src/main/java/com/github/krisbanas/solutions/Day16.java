package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * It's a mess and won't work. Sorry.
 */
public class Day16 {

    private String binaryString;

    public Day16() {
        final var input = FileHelper.loadString("Day16Input.txt");
        binaryString = buildBinaryString(input);
        part1();
        part2();
    }

    private String buildBinaryString(String input) {
        var sb = new StringBuilder();
        for (String s : input.split("")) {
            final var nonFormatted = Long.toString(Long.parseLong(s, 16), 2);
            long tmp = Long.parseLong(nonFormatted);
            sb.append(String.format("%04d", tmp));
        }
        return sb.toString();
    }

    public void part1() {
        final Packet rootPacket;
        rootPacket = parseBitsString();
        System.out.println(rootPacket.sumVersions());
    }

    private Packet parseBitsString() {
        if (binaryString.matches("0+") || binaryString.isEmpty()) {
            binaryString = "";
            return null;
        }
        final var version = Integer.parseInt(binaryString.substring(0, 3), 2);
        final var idType = Integer.parseInt(binaryString.substring(3, 6), 2);

        binaryString = binaryString.substring(6);

        if (idType == 4) {
            final var value = parseLiteral();
            return new LiteralValue(version, idType, value);
        } else {
            return processOperator(version, idType);
        }
    }

    private Packet processOperator(int version, int idType) {
        var lengthTypeId = binaryString.substring(0, 1);
        binaryString = binaryString.substring(1);
        if (lengthTypeId.equals("0")) {
            return processOperatorWithGivenLength(version, idType);
        } else {
            var number = binaryString.substring(0, 11);
            binaryString = binaryString.substring(11);
            long numberOfSubpackets = Long.parseLong(number, 2);
            List<Packet> packets = new ArrayList<>();
            for (long i = 0; i < numberOfSubpackets; i++) {
                var nestedPacket = parseBitsString();
                packets.add(nestedPacket);
            }
            return new Operator(version, idType, packets);
        }
    }

    private Packet processOperatorWithGivenLength(int version, int idType) {
        var stringStartLength = binaryString.length();
        var subPacketLength = Long.parseLong(binaryString.substring(0, 15));
        binaryString = binaryString.substring(15);

        List<Packet> packets = new ArrayList<>();
        while (binaryString.length() > stringStartLength - subPacketLength && !binaryString.isEmpty()) {
            var nestedPacket = parseBitsString();
            packets.add(nestedPacket);
        }
        return new Operator(version, idType, packets);
    }

    private long parseLiteral() {
        boolean end = false;
        final var sb = new StringBuilder();
        while (!end) {
            String frame = binaryString.substring(0, 5);
            if (frame.startsWith("0")) end = true;
            sb.append(frame.substring(1));
            this.binaryString = binaryString.substring(5);
        }
        return Long.parseLong(sb.toString(), 2);
    }

    public void part2() {
        final var rootPacket = parseBitsString();
        System.out.println(rootPacket.value());
    }

    private static abstract sealed class Packet permits LiteralValue, Operator {
        protected final int version;
        protected final int typeId;

        Packet(int version, int typeId) {
            this.version = version;
            this.typeId = typeId;
        }

        abstract int sumVersions();

        abstract long value();
    }

    private static final class LiteralValue extends Packet {
        private final long value;

        public LiteralValue(int version, int typeId, long value) {
            super(version, typeId);
            this.value = value;
        }

        @Override
        int sumVersions() {
            return version;
        }

        @Override
        long value() {
            return value;
        }
    }

    private static final class Operator extends Packet {
        private final List<Packet> subPackets;

        public Operator(int version, int typeId, List<Packet> subPackets) {
            super(version, typeId);
            this.subPackets = subPackets.stream().filter(Objects::nonNull).toList();
        }

        @Override
        int sumVersions() {
            return subPackets.stream().mapToInt(Packet::sumVersions).sum() + version;
        }

        @Override
        long value() {
            return switch (typeId) {
                case 0 -> subPackets.stream().mapToLong(Packet::value).sum();
                case 1 -> subPackets.stream().mapToLong(Packet::value).reduce(1L, (a, v) -> a * v);
                case 2 -> subPackets.stream().mapToLong(Packet::value).min().orElseThrow();
                case 3 -> subPackets.stream().mapToLong(Packet::value).max().orElseThrow();
                case 5 -> subPackets.get(0).value() > subPackets.get(1).value() ? 1L : 0L;
                case 6 -> subPackets.get(0).value() < subPackets.get(1).value() ? 1L : 0L;
                case 7 -> subPackets.get(0).value() == subPackets.get(1).value() ? 1L : 0L;
                default -> throw new IllegalStateException("unknown type: " + typeId);
            };
        }
    }
}
