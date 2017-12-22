package cn.ysk521.dto;

/**
 * Created by Y.S.K on 2017/10/24 in wx_bot_new.
 */
public class Result {
    private String error;
    private String title;

    public Result() {
    }

    public Result(String error, String title) {
        this.error = error;
        this.title = title;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Result{" +
                "error='" + error + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
