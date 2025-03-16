package com.abbas.notsee.dynamicweather;

import org.bukkit.World;

public class ClearWeather implements Weather {
    @Override
    public void apply(World world, int duration) {
        world.setStorm(false);
        world.setThundering(false);
        world.setWeatherDuration(duration);
    }

    @Override
    public void clear(World world) {
        world.setStorm(false);
        world.setThundering(false);
    }
}
