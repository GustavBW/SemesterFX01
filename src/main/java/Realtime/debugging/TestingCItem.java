package Realtime.debugging;

import BackEnd.PosPicCombo;
import Realtime.inventory.CItem;
import Realtime.inventory.Item;

public class TestingCItem extends CItem {
    public TestingCItem(Item item, PosPicCombo posPic) {
        super(item, posPic);
    }

    @Override
    public void onInstancedInter(){}
    @Override
    public void onInstancedRender(){}
}
