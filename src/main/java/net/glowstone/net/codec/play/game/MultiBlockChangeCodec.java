package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.glowstone.net.message.play.game.BlockChangeMessage;
import net.glowstone.net.message.play.game.MultiBlockChangeMessage;

import java.io.IOException;
import java.util.List;

public final class MultiBlockChangeCodec implements Codec<MultiBlockChangeMessage> {

    @Override
    public MultiBlockChangeMessage decode(ByteBuf buf) throws IOException {
        throw new DecoderException("Cannot decode MultiBlockChangeMessage");
    }

    @Override
    public ByteBuf encode(ByteBuf buf, MultiBlockChangeMessage message) throws IOException {
        List<BlockChangeMessage> records = message.getRecords();

        buf.writeInt(message.getChunkX());
        buf.writeInt(message.getChunkZ());

        int size = records.size();
        ByteBufUtils.writeVarInt(buf, size);

        for (int i = 0; i < size; i++) {
            BlockChangeMessage record = records.get(i);
            // XZY
            int pos = (record.getX() & 0xF) << 12
                    | (record.getZ() & 0xF) << 8 | record.getY() & 0xFF;
            buf.writeShort(pos);
            ByteBufUtils.writeVarInt(buf, record.getType());
        }
        return buf;
    }
}
