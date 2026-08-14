package com.example.smeltingtocrafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraftforge.fml.loading.FMLPaths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class RecipeGenerator {
    public static void generateAllRecipes() {
        try {
            var level = net.minecraft.client.Minecraft.getInstance().level;
            if (level == null) return;
            
            var recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING);
            Path outDir = FMLPaths.GAMEDIR.get().resolve("generated_recipes");
            Files.createDirectories(outDir);
            
            int count = 0;
            for (AbstractCookingRecipe recipe : recipes) {
                var input = recipe.getIngredients().get(0);
                if (input.getItems().length == 0) continue;
                var result = recipe.getResultItem(level.registryAccess());
                if (result.isEmpty()) continue;
                
                String inputId = BuiltInRegistries.ITEM.getKey(input.getItems()[0].getItem()).toString();
                String resultId = BuiltInRegistries.ITEM.getKey(result.getItem()).toString();
                
                JsonObject json = new JsonObject();
                json.addProperty("type", "minecraft:crafting_shapeless");
                JsonArray ingredients = new JsonArray();
                JsonObject ing = new JsonObject();
                ing.addProperty("item", inputId);
                ingredients.add(ing);
                json.add("ingredients", ingredients);
                JsonObject res = new JsonObject();
                res.addProperty("item", resultId);
                if (result.getCount() > 1) res.addProperty("count", result.getCount());
                json.add("result", res);
                
                String fileName = "craft_" + resultId.replace(":", "_") + ".json";
                Files.write(outDir.resolve(fileName), json.toString().getBytes(StandardCharsets.UTF_8));
                count++;
            }
            System.out.println("✅ 生成了 " + count + " 个配方文件到: " + outDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}