package de.cristelknight.doapi;

import de.cristelknight.doapi.common.registry.DoApiBlockEntityTypes;
import de.cristelknight.doapi.common.registry.DoApiEntityTypes;
import de.cristelknight.doapi.terraform.boat.item.TerraformBoatItemHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DoApi {
    public static final String MOD_ID = "doapi";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        DoApiEntityTypes.init();
        DoApiBlockEntityTypes.init();
    }

    public static void commonTerraformInit() {
        TerraformBoatItemHelper.registerDispenserBehaviours();
    }

}
