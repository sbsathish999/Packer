package com.mobiquity.packer;

import com.mobiquity.packer.exception.APIException;
import com.mobiquity.packer.exception.InvalidFormatException;
import com.mobiquity.packer.model.Item;
import com.mobiquity.packer.model.Package;
import com.mobiquity.packer.util.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/*
Strategy/Algorithm:
The class implements a dynamic programming approach to solve the knapsack problem,
where it determines the items to be included in the package based on weight and cost constraints.

Data Structure:
List<Package>: Represents the list of packages, where each package contains weight limit, items, and other relevant information.
List<Item>: Represents the list of items, where each item contains index number, weight, cost, and other details.
List<List<Item>>: Represents the subsets of items, where each subset is a combination of items for a package.

Design Pattern: The class doesn't explicitly utilize any specific design pattern.
However, it follows good practices of modularization and separation of concerns.
 */
public class Packer {

    public static String pack(String filePath) throws APIException {
        try{
            Stream<String> streamData = FileReader.loadFileContentStream(filePath);
            List<Package> listOfPackage =  convertToPackages(streamData);
            List<Package> packagesWithDifferentCombinationsOfItems = generatePackagesWithDifferentCombinationOfItems(listOfPackage);
            List<Package> filteredPackages = filterOutBestPackages(packagesWithDifferentCombinationsOfItems);
            return strFormattedPackages(filteredPackages);
        } catch (Throwable e){
            throw new APIException(e.getMessage(), e);
        }
    }

    private static List<Package> filterOutBestPackages(List<Package> packages) {
        for(Package pack : packages) {
            List<Item> filteredPackageItems = filterPackagesBasedOnWeightLimitAndHighestCost(pack);
            pack.setItemsToSend(filteredPackageItems);
        }
        return packages;
    }
    private static String strFormattedPackages(List<Package> packages) {
        String result = "";
        for(Package pack : packages) {
            if(pack.getItemsToSend().isEmpty()) {
                result += "-";
            } else {
                result += pack.getItemsToSend().stream()
                        .map(item -> item.getIndex().toString())
                        .collect(Collectors.joining(","));
            }
            result+="\n";
        }
        return result.trim();
    }

    private static List<Package> generatePackagesWithDifferentCombinationOfItems(List<Package> packages) {
        for(Package pack : packages) {
            List<List<Item>> subsets = findSubsets(pack.getItems());
            if (subsets.get(0).isEmpty())
                subsets.remove(0);
            pack.setCombinationOfItems(subsets);
        }
        return packages;
    }

    protected static List<Package> convertToPackages(Stream<String> lines) {
        List<Package> packages = new ArrayList<>();
        lines.forEach( line -> {
            Package  pack = null;
            try {
                pack = parsePackage(line);
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e);
            }
            packages.add(pack);
        });
        return packages;
    }

    private static Package parsePackage(String line) throws InvalidFormatException {
        String lineArray[] = line.split(":");
        if (lineArray.length != 2) {
            throw new InvalidFormatException("Invalid input format: " + line);
        }
        Double weightLimit = Double.parseDouble(lineArray[0].trim());
        String items[] = lineArray[1].trim().split("\\s");
        List<Item> itemList = parseItems(items);
        Package pack = new Package();
        pack.setWeightLimit(weightLimit);
        pack.setItems(itemList);
        return pack;
    }

    private static List<Item> parseItems(String[] items) throws InvalidFormatException {
        List<Item> itemList = new ArrayList<>();
        for(String item : items){
            String itemInfo[]= item.trim().replaceAll("[()]", "").split(",");
            if (itemInfo.length != 3) {
                throw new InvalidFormatException("Invalid input format: " + item);
            }
            String price = itemInfo[2].substring(1,itemInfo[2].length());
            Item packageItem = new Item(Integer.parseInt(itemInfo[0])
                    , Double.valueOf(itemInfo[1])
                    , Integer.valueOf(price));
            itemList.add(packageItem);
        }
        return itemList;
    }

    protected static List<Item> filterPackagesBasedOnWeightLimitAndHighestCost(Package pack) {
        List<Item> result = new ArrayList<>();
        Double weightLimit = pack.getWeightLimit();
        int highestCost = 0;
        double lowestWeight = 0.0;
        for(List<Item> items : pack.getCombinationOfItems()) {
            Integer totalCost = 0;
            Double totalWeight = 0.0;
            Boolean ignoreItems = false;
            for(Item item : items){
                totalCost += item.getCost();
                totalWeight += item.getWeight();
                if(totalWeight > weightLimit) {
                    ignoreItems = true;
                    break;
                }
            }
            if(!ignoreItems) {
                if(totalCost > highestCost) {
                    highestCost = totalCost;
                    lowestWeight = totalWeight;
                    result.clear();
                    result.addAll(items);
                } else if(totalCost == highestCost){
                    if(totalWeight < lowestWeight){
                        highestCost = totalCost;
                        lowestWeight = totalWeight;
                        result.clear();
                        result.addAll(items);
                    }
                }
            }
        }
        return result;
    }

    protected static List<List<Item>> findSubsets(List<Item> packageItems) {
        List<List<Item>> subsets = new ArrayList<>();
        subsets.add(new ArrayList<>());
        for (int i = 0; i < packageItems.size(); i++) {
            int n = subsets.size();
            for (int j = 0; j < n; j++) {
                List<Item> subset = new ArrayList<>(subsets.get(j));
                subset.add(packageItems.get(i));
                subsets.add(subset);
            }
        }
        return subsets;
    }
}