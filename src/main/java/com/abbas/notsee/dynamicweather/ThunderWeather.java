package com.abbas.notsee.dynamicweather;

import org.bukkit.World;

public class ThunderWeather implements Weather {
    @Override
    public void apply(World world, int duration) {
        world.setThundering(true);
        world.setWeatherDuration(duration);
    }

    @Override
    public void clear(World world) {
        world.setThundering(false);
    }
}
