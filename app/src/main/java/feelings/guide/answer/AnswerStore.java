package feelings.guide.answer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import feelings.guide.db.DbHelper;

import static android.provider.BaseColumns._ID;
import static feelings.guide.answer.AnswerContract.COLUMN_ANSWER;
import static feelings.guide.answer.AnswerContract.COLUMN_DATE_TIME;
import static feelings.guide.answer.AnswerContract.COLUMN_QUESTION_ID;
import static feelings.guide.answer.AnswerContract.ANSWER_TABLE;
import static feelings.guide.question.QuestionContract.COLUMN_IS_DELETED;
import static feelings.guide.question.QuestionContract.COLUMN_IS_HIDDEN;
import static feelings.guide.question.QuestionContract.QUESTION_TABLE;
import static feelings.guide.util.DateTimeUtil.DB_FORMATTER;

public class AnswerStore {

    public static boolean saveAnswer(Context context, Answer answer) {
        try (SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            if (answer.getId() > 0) {
                // id might be already set when previously deleted answer is being restored
                values.put(_ID, answer.getId());
            }
            values.put(COLUMN_QUESTION_ID, answer.getQuestionId());
            values.put(COLUMN_DATE_TIME, answer.getDateTime().format(DB_FORMATTER));
            values.put(COLUMN_ANSWER, answer.getAnswerText());

            long newRowId = db.insert(ANSWER_TABLE, null, values);
            boolean success = newRowId != -1;
            if (success) {
                answer.setId(newRowId);
            }
            return success;
        }
    }

    public static Cursor getAll(Context context) {
        SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();
        String[] projection = {_ID, COLUMN_QUESTION_ID, COLUMN_DATE_TIME, COLUMN_ANSWER};
        String orderBy = COLUMN_DATE_TIME + " DESC";

        return db.query(ANSWER_TABLE, projection, null, null, null, null, orderBy);
    }

    public static Cursor getByQuestionId(Context context, long questionId) {
        SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();
        String[] projection = {_ID, COLUMN_QUESTION_ID, COLUMN_DATE_TIME, COLUMN_ANSWER};
        String orderBy = COLUMN_DATE_TIME + " DESC";

        String selection = COLUMN_QUESTION_ID + " = ?";
        String[] args = {String.valueOf(questionId)};

        return db.query(ANSWER_TABLE, projection, selection, args, null, null, orderBy);
    }

    public static void deleteAll(Context context) {
        try (SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase()) {
            db.delete(ANSWER_TABLE, null, null);
        }
    }

    public static void deleteById(Context context, long id) {
        try (SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase()) {
            String where = _ID + " = ?";
            String[] args = {String.valueOf(id)};

            db.delete(ANSWER_TABLE, where, args);
        }
    }

    public static void deleteByQuestionId(Context context, long questionId) {
        try (SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase()) {
            String where = COLUMN_QUESTION_ID + " = ?";
            String[] args = {String.valueOf(questionId)};

            db.delete(ANSWER_TABLE, where, args);
        }
    }

    public static void deleteForDeletedQuestions(Context context) {
        try (SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase()) {
            db.execSQL("delete from " + ANSWER_TABLE + " where " + COLUMN_QUESTION_ID + " in (" +
                    "select " + _ID + " from " + QUESTION_TABLE + " where " +
                    COLUMN_IS_DELETED + " = 1 or " + COLUMN_IS_HIDDEN + " = 1" +
                    ")");
        }
    }

    public static boolean hasAnswers(Context context, long questionId) {
        try (Cursor cursor = getByQuestionId(context, questionId)) {
            return cursor.moveToFirst();
        }
    }
}
