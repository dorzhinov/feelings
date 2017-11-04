package feelings.guide.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import feelings.guide.R;

import static feelings.guide.answer.AnswerContract.SQL_CREATE_ANSWER_TABLE;
import static feelings.guide.question.QuestionContract.COLUMN_IS_DELETED;
import static feelings.guide.question.QuestionContract.COLUMN_IS_USER;
import static feelings.guide.question.QuestionContract.COLUMN_TEXT;
import static feelings.guide.question.QuestionContract.QUESTION_TABLE;
import static feelings.guide.question.QuestionContract.SQL_CREATE_QUESTION_TABLE;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeelingsGuide.db";

    @SuppressLint("StaticFieldLeak")
    private static DbHelper instance = null;

    private Context context;

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        db.execSQL(SQL_CREATE_ANSWER_TABLE);
        populateQuestions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void populateQuestions(SQLiteDatabase db) {
        populateQuestion(db, R.string.q_feelings);
        populateQuestion(db, R.string.q_insincerity);
        populateQuestion(db, R.string.q_gratitude);
        populateQuestion(db, R.string.q_preach);
        populateQuestion(db, R.string.q_lie);
        populateQuestion(db, R.string.q_irresponsibility);
        populateQuestion(db, R.string.q_do_body);
        populateQuestion(db, R.string.q_do_close);
        populateQuestion(db, R.string.q_do_others);
    }

    private void populateQuestion(SQLiteDatabase db, int resourceId) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_TEXT, context.getString(resourceId));
        values.put(COLUMN_IS_USER, false);
        values.put(COLUMN_IS_DELETED, false);

        db.insert(QUESTION_TABLE, null, values);
    }
}
