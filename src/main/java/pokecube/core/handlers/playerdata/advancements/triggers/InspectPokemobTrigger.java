package pokecube.core.handlers.playerdata.advancements.triggers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import pokecube.core.database.Database;
import pokecube.core.database.PokedexEntry;
import pokecube.core.interfaces.IPokemob;
import pokecube.core.interfaces.PokecubeMod;

public class InspectPokemobTrigger implements ICriterionTrigger<InspectPokemobTrigger.Instance>
{
    public static class Instance extends CriterionInstance
    {
        final PokedexEntry entry;

        public Instance(PokedexEntry entry)
        {
            super(InspectPokemobTrigger.ID);
            this.entry = entry != null ? entry : Database.missingno;
        }

        public boolean test(ServerPlayerEntity player, IPokemob pokemob)
        {
            return (this.entry == Database.missingno || pokemob.getPokedexEntry() == this.entry) && pokemob
                    .getOwner() != player;
        }

    }

    static class Listeners
    {
        private final PlayerAdvancements                                              playerAdvancements;
        private final Set<ICriterionTrigger.Listener<InspectPokemobTrigger.Instance>> listeners = Sets.<ICriterionTrigger.Listener<InspectPokemobTrigger.Instance>> newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn)
        {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public void add(ICriterionTrigger.Listener<InspectPokemobTrigger.Instance> listener)
        {
            this.listeners.add(listener);
        }

        public boolean isEmpty()
        {
            return this.listeners.isEmpty();
        }

        public void remove(ICriterionTrigger.Listener<InspectPokemobTrigger.Instance> listener)
        {
            this.listeners.remove(listener);
        }

        public void trigger(ServerPlayerEntity player, IPokemob pokemob)
        {
            List<ICriterionTrigger.Listener<InspectPokemobTrigger.Instance>> list = null;

            for (final ICriterionTrigger.Listener<InspectPokemobTrigger.Instance> listener : this.listeners)
                if (listener.getCriterionInstance().test(player, pokemob))
                {
                    if (list == null)
                        list = Lists.<ICriterionTrigger.Listener<InspectPokemobTrigger.Instance>> newArrayList();

                    list.add(listener);
                }
            if (list != null) for (final ICriterionTrigger.Listener<InspectPokemobTrigger.Instance> listener1 : list)
                listener1.grantCriterion(this.playerAdvancements);
        }
    }

    public static ResourceLocation ID = new ResourceLocation(PokecubeMod.ID, "inspect");

    private final Map<PlayerAdvancements, InspectPokemobTrigger.Listeners> listeners = Maps.<PlayerAdvancements, InspectPokemobTrigger.Listeners> newHashMap();

    public InspectPokemobTrigger()
    {
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn,
            ICriterionTrigger.Listener<InspectPokemobTrigger.Instance> listener)
    {
        InspectPokemobTrigger.Listeners bredanimalstrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (bredanimalstrigger$listeners == null)
        {
            bredanimalstrigger$listeners = new InspectPokemobTrigger.Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, bredanimalstrigger$listeners);
        }

        bredanimalstrigger$listeners.add(listener);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the
     * JSON.
     */
    @Override
    public InspectPokemobTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        final String name = json.has("entry") ? json.get("entry").getAsString() : "";
        return new InspectPokemobTrigger.Instance(Database.getEntry(name));
    }

    @Override
    public ResourceLocation getId()
    {
        return InspectPokemobTrigger.ID;
    }

    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.listeners.remove(playerAdvancementsIn);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn,
            ICriterionTrigger.Listener<InspectPokemobTrigger.Instance> listener)
    {
        final InspectPokemobTrigger.Listeners bredanimalstrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (bredanimalstrigger$listeners != null)
        {
            bredanimalstrigger$listeners.remove(listener);

            if (bredanimalstrigger$listeners.isEmpty()) this.listeners.remove(playerAdvancementsIn);
        }
    }

    public void trigger(ServerPlayerEntity player, IPokemob pokemob)
    {
        final InspectPokemobTrigger.Listeners bredanimalstrigger$listeners = this.listeners.get(player
                .getAdvancements());
        if (bredanimalstrigger$listeners != null) bredanimalstrigger$listeners.trigger(player, pokemob);
    }
}
