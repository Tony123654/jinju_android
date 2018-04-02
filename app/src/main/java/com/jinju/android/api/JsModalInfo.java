package com.jinju.android.api;

import java.util.List;

/**
 * Created by Libra on 2017/5/10.
 */

public class JsModalInfo {

    public String modalId;
    // 模态窗口标题
    public String title;
    // 模态窗口内容
    public String text;
    // 模态窗口类型 0-温馨提示 1-提交成功 2-提交失败
    public String type;
    //force-是否强制提示（禁止用户关闭该modal），值为“1”的时候，隐藏右上角“叉叉”，不传值或 其他非“1”值，则显示
    public int force ;
    // 模态窗口显示的按钮，Array，为1～3个，至少传递1个button
    public List<Buttons> buttons;

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public String getModalId() {
        return modalId;
    }

    public void setModalId(String modalId) {
        this.modalId = modalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Buttons> getButtons() {
        return buttons;
    }

    public void setButtons(List<Buttons> buttons) {
        this.buttons = buttons;
    }
    public static class Buttons {
        // 按钮显示的文本
        public String text;
        // 按钮值
        public String value;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Buttons{" +
                    "text='" + text + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JsModalInfo{" +
                "modalId='" + modalId + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", force=" + force +
                ", buttons=" + buttons +
                '}';
    }
}
