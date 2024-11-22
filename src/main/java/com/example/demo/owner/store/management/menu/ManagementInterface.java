package com.example.demo.owner.store.management.menu;

import com.example.demo.owner.store.management.StoreBasic;
import com.example.demo.owner.store.management.StoreTable;

public interface ManagementInterface {
     public StoreTable getTable(Long storeId);
     public StoreBasic findByStoreId(Long storeId);
}
