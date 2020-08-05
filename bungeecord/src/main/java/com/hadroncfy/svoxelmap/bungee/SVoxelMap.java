package com.hadroncfy.svoxelmap.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class SVoxelMap extends Plugin implements Listener {
    private static final String CHANNEL = "worldinfo:world_id";

    @Override
    public void onEnable() {
        super.onEnable();
        
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().registerChannel(CHANNEL);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event){
        if (CHANNEL.equals(event.getTag())){
            event.setCancelled(true);
            if (event.getSender() instanceof ProxiedPlayer){
                ProxiedPlayer player = (ProxiedPlayer)event.getSender();
                byte[] name = player.getServer().getInfo().getName().getBytes();
                byte[] data = new byte[name.length + 2];
                data[0] = 0;
                data[1] = (byte) name.length;
                for (int i = 0; i < name.length; i++){
                    data[i + 2] = name[i];
                }
                player.sendData(CHANNEL, data);
            }
        }
    }

}