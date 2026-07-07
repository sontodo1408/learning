import { useDialogStore } from "@/stores/dialog-store";
import { shallowRef } from "vue";
import { DIALOG_BTN } from "@/helpers/const";
import { v4 as uuid } from "uuid";
/**
 * Config:
 * 1. showHeader
 * 2. isComponent
 * 3. buttons
 * 4. height
 * 5. width
 * 6. params: params truyền vào khi sử dụng component
 */
class DialogCommon {
  // ポップアップ表示
  show = (title, content, config) =>
    new Promise((resolve) => {
      // ダイアログのタイトル、内容、設定を行う
      const item = {
        id: uuid(),
        title,
        content,
        config,
        show: true,
        close: resolve,
      };
      const dialog = useDialogStore();
      dialog.show(item);
    });

  // ポップアップをコンポーネントとして表示する
  showContent = (title, content, config = {}) => {
    const component = shallowRef(content);
    config = {
      showHeader: false,
      isComponent: true,
      ...config,
    };
    return this.show(title, component, config);
  };

  // ポップアップを通知として表示する
  showMessage = (title, content, config = {}) => {
    config = {
      showHeader: true,
      buttons: [DIALOG_BTN.CLOSE],
      isComponent: false,
      isUseI18n: false,
      ...config,
    };
    return this.show(title, content, config);
  };

  // ポップアップを確認として表示する
  showConfirm = (title, content, config = {}) => {
    config = {
      showHeader: true,
      buttons: [DIALOG_BTN.NO, DIALOG_BTN.YES],
      isComponent: false,
      isUseI18n: false,
      ...config,
    };
    return this.show(title, content, config);
  };

  // ポップアップを閉じて結果を返す
  hide = (id, value) => {
    const dialog = useDialogStore();
    const current = dialog.findById(id);

    // ポップアップが存在しない場合、処理を中止する
    if (!current) {
      return;
    }
    current.show = false;
    // promiseを終了し、値を返してポップアップを非表示する
    setTimeout(() => {
      current.close(value);
      dialog.hide(id);
    }, 150);
  };
}

export default new DialogCommon();
