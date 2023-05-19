package com.mobiquity.packer.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Package {
    Double weightLimit;
    List<Item> items;
    List<List<Item>> combinationOfItems;
    List<Item> itemsToSend;
}
