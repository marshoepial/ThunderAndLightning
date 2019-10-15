package com.CCraze.LightningCraft.config;

import com.CCraze.LightningCraft.LightningCraft;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

import static com.CCraze.LightningCraft.setup.ModVals.*;

public class LightningCraftConfig {
    private final ConfigItem creativeIsDisabled = new ConfigItem("Creative IsDisabled", false);
    private final ConfigItem creativeChanceToStrike = new ConfigItem("Creative ChanceToStrike", 1.0);
    private final ConfigItem creativeMaxStrikeDistance = new ConfigItem("Creative MaxStrikeDistance", 99999);
    private final ConfigItem creativeMaxEnergy = new ConfigItem("Creative MaxEnergy", 40000);
    private final ConfigItem creativeCanStoreEnergy = new ConfigItem("Creative CanStoreEnergy", true);

    private final ConfigItem ironIsDisabled = new ConfigItem("Iron IsDisabled", false);
    private final ConfigItem ironChanceToStrike = new ConfigItem("Iron ChanceToStrike", 0.5);
    private final ConfigItem ironMaxStrikeDistance = new ConfigItem("Iron MaxStrikeDistance", 200);
    private final ConfigItem ironMaxEnergy = new ConfigItem("Iron MaxEnergy", 15000);
    private final ConfigItem ironCanStoreEnergy = new ConfigItem("Iron CanStoreEnergy", true);

    private final ConfigItem diamondIsDisabled = new ConfigItem("Diamond IsDisabled", false);
    private final ConfigItem diamondChanceToStrike = new ConfigItem("Diamond ChanceToStrike", 0.7);
    private final ConfigItem diamondMaxStrikeDistance = new ConfigItem("Diamond MaxStrikeDistance", 600);
    private final ConfigItem diamondMaxEnergy = new ConfigItem("Diamond MaxEnergy", 40000);
    private final ConfigItem diamondCanStoreEnergy = new ConfigItem("Diamond CanStoreEnergy", true);

    private final ConfigItem woolIsDisabled = new ConfigItem("Wool IsDisabled", false);
    private final ConfigItem woolChanceToStrike = new ConfigItem("Wool ChanceToStrike", 0.5);
    private final ConfigItem woolMaxStrikeDistance = new ConfigItem("Wool MaxStrikeDistance", 100);
    private final ConfigItem woolMaxEnergy = new ConfigItem("Wool MaxEnergy", 0);
    private final ConfigItem woolCanStoreEnergy = new ConfigItem("Wool CanStoreEnergy", false);

    private final ConfigGroup creativeLightningAttractorGroup = new ConfigGroup("Creative Lightning Attractor",
            new ConfigItem[]{creativeIsDisabled, creativeChanceToStrike, creativeMaxStrikeDistance, creativeMaxEnergy, creativeCanStoreEnergy}, 2);
    private final ConfigGroup ironLightningAttractorGroup = new ConfigGroup("Iron Lightning Attractor",
            new ConfigItem[]{ironIsDisabled, ironChanceToStrike, ironMaxStrikeDistance, ironMaxEnergy, ironCanStoreEnergy}, 2);
    private final ConfigGroup diamondLightningAttractorGroup = new ConfigGroup("Diamond Lightning Attractor",
            new ConfigItem[]{diamondIsDisabled, diamondChanceToStrike, diamondMaxStrikeDistance, diamondMaxEnergy, diamondCanStoreEnergy}, 2);
    private final ConfigGroup woolLightningAttractorGroup = new ConfigGroup("Wool Lightning Attractor",
            new ConfigItem[]{woolIsDisabled, woolChanceToStrike, woolMaxStrikeDistance, woolMaxEnergy, woolCanStoreEnergy}, 2);
    private final ConfigGroup lightningAttractorGroup = new ConfigGroup("Lightning Attractors",
            new Object[]{creativeLightningAttractorGroup, ironLightningAttractorGroup, diamondLightningAttractorGroup, woolLightningAttractorGroup}, 1);
    private final ConfigGroup mainGroup = new ConfigGroup("LightningCraft Config", new ConfigGroup[]{lightningAttractorGroup}, 0);


    public LightningCraftConfig(){
        if (!CONFIGFILE.exists()){
            if (!new File("config/"+ LightningCraft.MODID).exists()) new File("config/"+LightningCraft.MODID).mkdirs();
            writeToConfig(mainGroup);
        }
        if (!RECIPEFILE.exists()) {
            try {
                RECIPEFILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } public void writeToConfig(ConfigGroup group) {
        try{
            FileWriter writer = new FileWriter(CONFIGFILE);
            writer.write(group.getOutputString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } public Object readFromConfig(String name){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(CONFIGFILE));
            String line = reader.readLine();
            while (line != null){
                if (line.contains("=")){
                    Object results =  mainGroup.findValue(name, line);
                    if (results != null) return results;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ConfigGroup{
        private final String outputString;
        private final Object[] configItems;
        public ConfigGroup(String name, Object[] configItems, int indent){
            this.configItems = configItems;
            StringBuilder sb = new StringBuilder(name).append(":\n");
            String indentStr = StringUtils.repeat("     ", indent);
            for (Object currentItem : configItems) {
                if (currentItem instanceof ConfigItem)
                    sb.append(indentStr).append(((ConfigItem) currentItem).getOutputString()).append("\n");
                else if (currentItem instanceof ConfigGroup)
                    sb.append(indentStr).append(((ConfigGroup) currentItem).getOutputString()).append("\n");
            }
            outputString = sb.toString();
        } public Object findValue(String name, String line){
            for (Object currItem : configItems) {
                if (currItem instanceof ConfigItem) {
                    Object findItemResult = ((ConfigItem) currItem).findItem(line, name);
                    if (findItemResult != null) {
                        return findItemResult;
                    }
                }
            } for (Object currItem : configItems) {
                if (currItem instanceof ConfigGroup){
                    Object returnedObject = ((ConfigGroup) currItem).findValue(name, line);
                    if (returnedObject != null) return returnedObject;
                }
            }
            return null;
        }
        public String getOutputString() { return outputString; }
    }
    public static class ConfigItem{
        private final String outputString;
        private String itemType;
        private String name;
        public ConfigItem(String name, Object item){
            this.name = name;
            System.out.println("Config option "+name+" parsed with class "+item.getClass());
            StringBuilder sb = new StringBuilder(name);
            if (item instanceof Boolean)
                itemType = "B:";
            else if (item instanceof Integer)
                itemType = "I:";
            else if (item instanceof String)
                itemType = "S:";
            else if (item instanceof Double)
                itemType = "D:";
            sb.insert(0, itemType).append("=").append(item);
            outputString = sb.toString();
        } public Object findItem(String currLine, String name){
            System.out.println("Comparing "+name+" with "+this.name+" and line"+currLine);
            if (currLine.contains(this.name) && currLine.contains(itemType) && this.name.equals(name)){
                System.out.println("Matched!");
                final String substring = currLine.replaceAll(" {5}", "").substring(3 + name.length());
                Object returningObj = null;
                if (itemType.equals("B:")) returningObj = Boolean.parseBoolean(substring);
                if (itemType.equals("I:")) returningObj = Integer.parseInt(substring);
                if (itemType.equals("S:")) returningObj = substring;
                if (itemType.equals("D:")) returningObj = Double.parseDouble(substring);
                System.out.println("Returning "+returningObj);
                return returningObj;
            }
            return null;
        } public String getOutputString(){ return outputString; }
    }
}
