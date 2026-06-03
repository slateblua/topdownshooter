package com.slateblua.roargame.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slateblua.roargame.core.Locator;
import com.slateblua.roargame.data.GameData;
import com.slateblua.roargame.gamecomponent.Player;
import com.slateblua.roargame.gamecomponent.weapon.WeaponData;
import lombok.Getter;

public class GameUI {
    private final Stage stage;
    @Getter
    private final Table rootTable;

    private static GameUI gameUI;

    public static GameUI get () {
        if (gameUI == null) {
            gameUI = new GameUI();
        }
        return gameUI;
    }

    private GameUI () {
        stage = new Stage(new ScreenViewport());

        final Table weaponsTable = createWeaponsComponent();

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.add(weaponsTable).expand().bottom();

        stage.addActor(rootTable);

        final Table gamePadTable = createGamePadTable();
        rootTable.addActor(gamePadTable);

        // init dialog manager
        final Table dialogContainer = new Table();
        dialogContainer.setFillParent(true);

        stage.addActor(dialogContainer);

        Gdx.input.setInputProcessor(stage);
    }

    private Table createGamePadTable () {
        final Table controllerTable = new Table();
        controllerTable.setFillParent(true);
        final GamePad actor = new GamePad();

        controllerTable.add(actor).expand().bottom().size(100).padBottom(250);

        return controllerTable;
    }

    private Table createWeaponsComponent () {
        final Table weaponsTable = new Table();
        weaponsTable.pad(20);
        weaponsTable.defaults().space(20).size(130);
        final Array<WeaponData> weapons = Locator.get(GameData.class).getWeapons();

        for (WeaponData weapon : weapons) {
            final WeaponButton weaponButton = new WeaponButton(weapon, data ->  Player.get().setCurrentWeapon(Locator.get(GameData.class).getWeaponMap().get(data)));
            weaponsTable.add(weaponButton);
        }

        return weaponsTable;
    }

    public void render () {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose () {
        stage.dispose();
    }
}
