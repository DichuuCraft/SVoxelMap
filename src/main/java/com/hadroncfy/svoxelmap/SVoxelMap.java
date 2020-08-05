package com.hadroncfy.svoxelmap;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent.ForwardResult;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import org.slf4j.Logger;

@Plugin(
    id = "svoxelmap",
    name = "SVoxelmap",
    description = "Voxelmap server-side support",
    version = "0.1",
    authors = "hadroncfy"
)
public class SVoxelMap {
    @Inject
    private ProxyServer server;
    @Inject
    private Logger logger;

    private static final ChannelIdentifier VOXELMAP_CHANNEL = MinecraftChannelIdentifier.create("worldinfo", "world_id");

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event){
        server.getChannelRegistrar().register(VOXELMAP_CHANNEL);
    }
    @Subscribe
    public void onPluginMessage(PluginMessageEvent event){
        ChannelMessageSource src = event.getSource();
        if (VOXELMAP_CHANNEL.equals(event.getIdentifier())){
            event.setResult(ForwardResult.handled());
            if (src instanceof Player){
                Player player = (Player)src;
                player.getCurrentServer().ifPresent(c -> {
                    byte[] name = c.getServerInfo().getName().getBytes(StandardCharsets.UTF_8);
                    byte[] data = new byte[name.length + 2];
                    data[0] = 0;
                    data[1] = (byte) name.length;
                    for (int i = 0; i < name.length; i++){
                        data[i + 2] = name[i];
                    }
                    player.sendPluginMessage(VOXELMAP_CHANNEL, data);
                });
            }
        }
    }
    
}