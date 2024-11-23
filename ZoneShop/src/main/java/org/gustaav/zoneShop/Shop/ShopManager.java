package org.gustaav.zoneShop.Shop;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneShop.ZoneShop;
import org.gustaav.zoneShop.dao.FileManager;

import java.util.*;

public class ShopManager implements Listener {

    private final FileManager fileManager;
    private final List<ShopProduct> blocos = new ArrayList<>();
    private final List<ShopProduct> ferramentas = new ArrayList<>();
    private final List<ShopProduct> plantacao = new ArrayList<>();
    private final List<ShopProduct> utilitarios = new ArrayList<>();
    private final List<ShopProduct> outros = new ArrayList<>();

    public ShopManager(FileManager fileManager, ZoneShop plugin) {
        this.fileManager = fileManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void loadCategories() {
        loadCategoryProducts("blocos", blocos);
        loadCategoryProducts("ferramentas", ferramentas);
        loadCategoryProducts("plantacao", plantacao);
        loadCategoryProducts("utilitarios", utilitarios);
        loadCategoryProducts("outros", outros);
    }
    public void saveCategories() {
        saveCategory("blocos", blocos);
        saveCategory("ferramentas", ferramentas);
        saveCategory("plantacao", plantacao);
        saveCategory("utilitarios", utilitarios);
        saveCategory("outros", outros);
    }

    private void loadCategoryProducts(String categoryName, List<ShopProduct> categoryList) {
        categoryList.clear();
        FileConfiguration config = fileManager.loadFile(categoryName);
        Set<String> keys = config.getKeys(false);

        for (String key : keys) {
            String name = config.getString(key + ".name");
            Material material = Material.getMaterial(key.toUpperCase());
            int price = config.getInt(key + ".price");

            if (material != null) {
                ShopProduct product = new ShopProduct(name, material, price);
                categoryList.add(product);
            } else {
                System.out.println("Material não encontrado: " + key);
            }
        }
    }

    public void addProduct(String categoria, Material material, int price, String name, Player player) {
        switch(categoria) {
            case "blocos":
                for(ShopProduct produto : blocos) {
                    if(produto.getMaterial() == material) {
                        produto.setName(name);
                        produto.setPrice(price);
                        player.sendMessage("§aBloco alterado para o preco §f" + price + "§a e o nome §f" + name + "§a.");
                        return;
                    }
                }
                ShopProduct novoBloco = new ShopProduct(name, material, price);
                blocos.add(novoBloco);
                player.sendMessage("§aBloco adicionado para o preco §f" + price + "§a e o nome §f" + name + "§a.");
                return;
            case "ferramentas":
                for(ShopProduct produto : ferramentas) {
                    if(produto.getMaterial() == material) {
                        produto.setName(name);
                        produto.setPrice(price);
                        player.sendMessage("§aFerramenta alterada para o preco §f" + price + "§a e o nome §f" + name + "§a.");
                        return;
                    }
                }
                ShopProduct novaFerramenta = new ShopProduct(name, material, price);
                ferramentas.add(novaFerramenta);
                player.sendMessage("§aFerramenta adicionada para o preco §f" + price + "§a e o nome §f" + name + "§a.");
                return;
            case "plantacao":
                for(ShopProduct produto : plantacao) {
                    if(produto.getMaterial() == material) {
                        produto.setName(name);
                        produto.setPrice(price);
                        player.sendMessage("§aPlantacao alterada para o preco §f" + price + "§a e o nome §f" + name + "§a.");
                        return;
                    }
                }
                ShopProduct novaPlantacao = new ShopProduct(name, material, price);
                plantacao.add(novaPlantacao);
                player.sendMessage("§aPlantacao adicionada para o preco §f" + price + "§a e o nome §f" + name + "§a.");
                return;
            case "utilitarios":
                for(ShopProduct produto : utilitarios) {
                    if(produto.getMaterial() == material) {
                        produto.setName(name);
                        produto.setPrice(price);
                        player.sendMessage("§aUtilitario alterada para o preco §f" + price + "§a e o nome §f" + name + "§a.");
                        return;
                    }
                }
                ShopProduct novoUtilitario = new ShopProduct(name, material, price);
                utilitarios.add(novoUtilitario);
                player.sendMessage("§aUtilitario adicionada para o preco §f" + price + "§a e o nome §f" + name + "§a.");
                return;
            case "outros":
                for(ShopProduct produto : outros) {
                    if(produto.getMaterial() == material) {
                        produto.setName(name);
                        produto.setPrice(price);
                        player.sendMessage("§aOutro produto alterado para o preco §f" + price + "§a e o nome §f" + name + "§a.");
                        return;
                    }
                }
                ShopProduct novoOutro = new ShopProduct(name, material, price);
                outros.add(novoOutro);
                player.sendMessage("§aOutro produto adicionada para o preco §f" + price + "§a e o nome §f" + name + "§a.");
                return;
            default:
                player.sendMessage("§cEssa categoria não existe. Tente novamente");
                break;
        }
    }

    public void removeProduct(String categoria, Material material, Player player) {
        switch(categoria) {
            case "blocos":
                for(ShopProduct produto : blocos) {
                    if(produto.getMaterial() == material) {
                        blocos.remove(produto);
                        player.sendMessage("§cProduto removido com sucesso.");
                        return;
                    }
                }
                player.sendMessage("§cEsse produto não pertence a essa categoria.");
                return;
            case "ferramentas":
                for(ShopProduct produto : ferramentas) {
                    if(produto.getMaterial() == material) {
                        blocos.remove(produto);
                        player.sendMessage("§cProduto removido com sucesso.");
                        return;
                    }
                }
                player.sendMessage("§cEsse produto não pertence a essa categoria.");
                return;
            case "plantacao":
                for(ShopProduct produto : plantacao) {
                    if(produto.getMaterial() == material) {
                        blocos.remove(produto);
                        player.sendMessage("§cProduto removido com sucesso.");
                        return;
                    }
                }
                player.sendMessage("§cEsse produto não pertence a essa categoria.");
                return;
            case "utilitarios":
                for(ShopProduct produto : utilitarios) {
                    if(produto.getMaterial() == material) {
                        blocos.remove(produto);
                        player.sendMessage("§cProduto removido com sucesso.");
                        return;
                    }
                }
                player.sendMessage("§cEsse produto não pertence a essa categoria.");
                return;
            case "outros":
                for(ShopProduct produto : outros) {
                    if(produto.getMaterial() == material) {
                        blocos.remove(produto);
                        player.sendMessage("§cProduto removido com sucesso.");
                        return;
                    }
                }
                player.sendMessage("§cEsse produto não pertence a essa categoria.");
                return;
            default:
                player.sendMessage("§cEssa categoria não existe. Tente novamente");
                break;
        }
    }

    private void saveCategory(String categoryName, List<ShopProduct> categoryList) {
        FileConfiguration config = fileManager.loadFile(categoryName);

        for (ShopProduct product : categoryList) {
            String key = product.getMaterial().name().toLowerCase();
            config.set(key + ".name", product.getName());
            config.set(key + ".price", product.getPrice());
        }

        fileManager.saveFile(categoryName, config);
    }

    public List<ShopProduct> getBlocos() {
        return blocos;
    }
    public List<ShopProduct> getFerramentas() {
        return ferramentas;
    }
    public List<ShopProduct> getPlantacao() {
        return plantacao;
    }
    public List<ShopProduct> getUtilitarios() {
        return utilitarios;
    }
    public List<ShopProduct> getOutros() {
        return outros;
    }

    /*
    Processar Compra
     */
    private final Map<UUID, ShopProduct> produtos = new HashMap<>();
    private final Map<UUID, Integer> quantidades = new HashMap<>();

    public void buyProduct(Player player, ShopProduct product) {
        UUID playerUUID = player.getUniqueId();
        produtos.put(playerUUID, product);
        player.sendMessage("");
        player.sendMessage("  §7Produto sendo adquirido: §f" + product.getName() );
        player.sendMessage("  §eDigite a quantidade para continuar, ou use §6§ncancelar§r§e.");
        player.sendMessage("");
    }
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (produtos.containsKey(playerUUID)) {
            event.setCancelled(true);
            try {
                int quantity = Integer.parseInt(event.getMessage());
                quantidades.put(playerUUID, quantity);
                processPurchase(player, produtos.get(playerUUID), quantity);
                produtos.remove(playerUUID);
                quantidades.remove(playerUUID);
            } catch (NumberFormatException e) {
                player.sendMessage("§cPor favor, insira um número válido.");
            }
        }
    }
    private void processPurchase(Player player, ShopProduct product, int quantity) {
        int price = product.getPrice() * quantity;
        UUID playerUUID = player.getUniqueId();

        if (ZoneShop.getEcon().has(player, price)) {
            if (hasInventorySpace(player, product.getMaterial(), quantity)) {
                ZoneShop.getEcon().withdrawPlayer(player, price);
                Material material = product.getMaterial();
                player.sendMessage("§aVocê comprou §fx" + quantity + " §7" + product.getName() + "§a por §2$§f" + ZoneShop.format(price) + "§a coins.");
                while (quantity > 0) {
                    int amount = Math.min(quantity, material.getMaxStackSize());
                    player.getInventory().addItem(new ItemStack(material, amount));
                    quantity -= amount;
                }
            } else {
                player.sendMessage("§cVocê não tem espaço suficiente no inventário.");
            }
        } else {
            player.sendMessage("§cVocê não tem dinheiro suficiente.");
        }
    }
    private boolean hasInventorySpace(Player player, Material material, int quantity) {
        int space = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                space += material.getMaxStackSize();
            } else if (item.getType() == material) {
                space += material.getMaxStackSize() - item.getAmount();
            }
            if (space >= quantity) {
                return true;
            }
        }
        return space >= quantity;
    }

}
