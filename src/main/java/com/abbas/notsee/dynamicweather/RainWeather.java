package com.abbas.notsee.dynamicweather;

import org.bukkit.World;

public class RainWeather implements Weather {
    @Override
    public void apply(World world, int duration) {
        world.setStorm(true);
        world.setWeatherDuration(duration);
    }

    @Override
    public void clear(World world) {
        world.setStorm(false);
    }
}
