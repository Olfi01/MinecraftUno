package de.crazypokemondev.mcpaper.games.minecraftuno.helpers;

public class Callback<T> {
    private T object;
    private boolean returnedNull = false;

    public T get() {
        T object = this.object;
        this.object = null;
        this.returnedNull = false;
        return object;
    }

    public void set(T object) {
        if (object == null) returnedNull = true;
        this.object = object;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasReturned() {
        return object != null && !returnedNull;
    }
}
