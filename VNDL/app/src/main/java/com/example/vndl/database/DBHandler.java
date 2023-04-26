package com.example.vndl.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.vndl.model.Question;
import com.example.vndl.model.QuestionType;
import com.example.vndl.model.Sign;
import com.example.vndl.model.SignGroup;
import com.example.vndl.model.Test;
import com.example.vndl.model.TestResult;
import com.example.vndl.model.TestResultDetail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private final static String DATABASE_NAME="gplxa1.db";
    private final static int DATABASE_VERSION=1;
    private static String DATABASE_PATH = "";
    private Context context;

    public DBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).toString();
        createDatabase();

    }
    public List<SignGroup> getListSignGroup() {
        List<SignGroup> signGroups = new ArrayList<>();
        String sql="select * from signGroup";
        SQLiteDatabase st=getReadableDatabase();
        Cursor rs=st.rawQuery(sql,null);

        while(rs!=null && rs.moveToNext()){
            SignGroup signGroup=new SignGroup(rs.getInt(0), rs.getString(1));

            List<Sign> signs = new ArrayList<>();
            sql="select * from Sign where groupID = ?";
            String[] agrs={String.valueOf(signGroup.getId())};
            Cursor rsSign=st.rawQuery(sql,agrs);

            while(rsSign!=null && rsSign.moveToNext()){
                int id = rsSign.getInt(0);
                int groupID = rsSign.getInt(1);
                String signCode = rsSign.getString(2);
                String title = rsSign.getString(3);
                String desc = rsSign.getString(4);
                String imageName = rsSign.getString(5);

                Sign sign = new Sign(id, groupID, signCode, title, desc, imageName);
                signs.add(sign);
            }
            signGroup.setSigns(signs);
            signGroups.add(signGroup);
        }
        rs.close();
        return signGroups;
    }

    public List<QuestionType> getQuestionType() {
        List<QuestionType> questionTypes=new ArrayList<>();
        String sql="select * from QuestionType";
        SQLiteDatabase st=getReadableDatabase();
        Cursor rs=st.rawQuery(sql,null);
        while(rs!=null && rs.moveToNext()){
            int id = rs.getInt(0);
            String title = rs.getString(1);
            String desc = rs.getString(2);
            int totalQuestion = rs.getInt(3);
            String imageName = rs.getString(4);

            QuestionType questionType=new QuestionType(id,title,desc,totalQuestion,imageName);
            questionTypes.add(questionType);
        }
        rs.close();
        return questionTypes;
    }

    public List<Question> getListQuestion(int questionTypeID) {
        List<Question> questions=new ArrayList<>();
        String sql="select * from Question where questionType_id = ?";
        String[] agrs={String.valueOf(questionTypeID)};
        SQLiteDatabase st=getReadableDatabase();
        Cursor rs=st.rawQuery(sql,agrs);

        while(rs!=null && rs.moveToNext()){
            int id = rs.getInt(0);
            int questionType_id = rs.getInt(1);
            String questionText  = rs.getString(2);
            String questionImageName  = rs.getString(3);
            String answer_1  = rs.getString(4);
            String answer_2  = rs.getString(5);
            String answer_3  = rs.getString(6);
            String answer_4  = rs.getString(7);
            int answer_correct = rs.getInt(8);
            String explanation = rs.getString(9);
            int question_die = rs.getInt(10); // 0:False - 1:True
            int practice_answer = rs.getInt(11);

            Question question = new Question(id,questionType_id,questionText,questionImageName,answer_1,answer_2,answer_3,answer_4,answer_correct,explanation,question_die,practice_answer);
            questions.add(question);
        }
        rs.close();
        return questions;
    }

    public List<Question> getAllQuestion() {
        List<Question> questions=new ArrayList<>();
        String sql="select * from Question";
        SQLiteDatabase st=getReadableDatabase();
        Cursor rs=st.rawQuery(sql,null);

        while(rs!=null && rs.moveToNext()){
            int id = rs.getInt(0);
            int questionType_id = rs.getInt(1);
            String questionText  = rs.getString(2);
            String questionImageName  = rs.getString(3);
            String answer_1  = rs.getString(4);
            String answer_2  = rs.getString(5);
            String answer_3  = rs.getString(6);
            String answer_4  = rs.getString(7);
            int answer_correct = rs.getInt(8);
            String explanation = rs.getString(9);
            int question_die = rs.getInt(10); // 0:False - 1:True
            int practice_answer = rs.getInt(11);

            Question question = new Question(id,questionType_id,questionText,questionImageName,answer_1,answer_2,answer_3,answer_4,answer_correct,explanation,question_die,practice_answer);
            questions.add(question);
        }
        rs.close();
        return questions;
    }

    public void updateQuestion(Question question) {
        String sql = "update Question set practice_answer = ? where id = ?";
        String[] args = {String.valueOf(question.getPractice_answer()), String.valueOf(question.getId())};
        SQLiteDatabase st = getWritableDatabase();
        st.execSQL(sql,args);
    }

    public List<Test> getListTest(){
        List<Test> tests=new ArrayList<>();
        String sql="select * from Test";
        SQLiteDatabase st=getReadableDatabase();
        Cursor rs=st.rawQuery(sql,null);

        while(rs!=null && rs.moveToNext()){
            int testID = rs.getInt(0);
            String testName = rs.getString(1);
            TestResult testResult = null;
            String sqlTestResult = "select * from TestResult where testID = ?";
            String[] args = {String.valueOf(testID)};
            Cursor rsTestResult = st.rawQuery(sqlTestResult,args);

            if(rsTestResult!=null && rsTestResult.moveToNext()){
                int numberAnswerCorrect = rsTestResult.getInt(1);
                int numberAnswerIncorrect = rsTestResult.getInt(2);
                int numberAnswerRemaining = rsTestResult.getInt(3);
                int totalQuestion = rsTestResult.getInt(4);
                int minimumAnswerCorrect = rsTestResult.getInt(5);
                int isFailDieQuestion = rsTestResult.getInt(6);

                testResult = new TestResult(testID, numberAnswerCorrect, numberAnswerIncorrect, numberAnswerRemaining, totalQuestion, minimumAnswerCorrect, isFailDieQuestion);
            }

            Test test = new Test(testID, testName, testResult);
            tests.add(test);
        }
        rs.close();
        return tests;
    }

    public List<Question> getListQuestionOfTest(int testID){
        List<Question> questions=new ArrayList<>();
        String sql="select * from Question inner join TestQuestion on TestQuestion.q_id = Question.id where TestQuestion.t_id = ?";
        String[] agrs={String.valueOf(testID)};
        SQLiteDatabase st=getReadableDatabase();
        Cursor rs=st.rawQuery(sql,agrs);

        while(rs!=null && rs.moveToNext()){
            int id = rs.getInt(0);
            int questionType_id = rs.getInt(1);
            String questionText  = rs.getString(2);
            String questionImageName  = rs.getString(3);
            String answer_1  = rs.getString(4);
            String answer_2  = rs.getString(5);
            String answer_3  = rs.getString(6);
            String answer_4  = rs.getString(7);
            int answer_correct = rs.getInt(8);
            String explanation = rs.getString(9);
            int question_die = rs.getInt(10); // 0:False - 1:True
            int practice_answer = rs.getInt(11);

            Question question = new Question(id,questionType_id,questionText,questionImageName,answer_1,answer_2,answer_3,answer_4,answer_correct,explanation,question_die,practice_answer);
            questions.add(question);
        }
        rs.close();
        return questions;
    }

    public void saveTestResult(TestResult testResult){
        String sqlCheckExist="select 1 from TestResult where testID = ? limit 1";
        String[] agrsCheckExist={String.valueOf(testResult.getTestID())};
        SQLiteDatabase st=getReadableDatabase();
        Cursor rsCheckExist=st.rawQuery(sqlCheckExist,agrsCheckExist);

        if (rsCheckExist!=null && rsCheckExist.moveToNext()) {
            //Nếu exits -> update
            String sql="update TestResult set numberAnswerCorrect = ?, numberAnswerIncorrect = ?, numberAnswerRemaining = ?, isFailDieQuestion = ? where testID = ?";
            String[] agrs={
                    String.valueOf(testResult.getNumberAnswerCorrect()),
                    String.valueOf(testResult.getNumberAnswerIncorrect()),
                    String.valueOf(testResult.getNumberAnswerRemaining()),
                    String.valueOf(testResult.getIsFailDieQuestion()),
                    String.valueOf(testResult.getTestID())};
            SQLiteDatabase stUpdate = getWritableDatabase();
            stUpdate.execSQL(sql,agrs);
        } else {
            //Nếu k exits -> insert
            String sql="insert into TestResult (testID, numberAnswerCorrect, miniumAnswerCorrect, totalQuestion, numberAnswerIncorrect, numberAnswerRemaining, isFailDieQuestion) " +
                    "values (?, ?, ?, ?, ?, ?, ?)";
            String[] agrs={
                    String.valueOf(testResult.getTestID()),
                    String.valueOf(testResult.getNumberAnswerCorrect()),
                    String.valueOf(testResult.getMinimumAnswerCorrect()),
                    String.valueOf(testResult.getTotalQuestion()),
                    String.valueOf(testResult.getNumberAnswerIncorrect()),
                    String.valueOf(testResult.getNumberAnswerRemaining()),
                    String.valueOf(testResult.getIsFailDieQuestion())};
            SQLiteDatabase stInsert = getWritableDatabase();
            stInsert.execSQL(sql,agrs);
        }
        rsCheckExist.close();
        return;
    }

    public void saveTestResultDetail(TestResultDetail testResultDetail){
        String sqlCheckExist="select 1 from TestResultDetail where testID = ? and questionID = ? limit 1";
        String[] agrsCheckExist={String.valueOf(testResultDetail.getTestID()), String.valueOf(testResultDetail.getQuestionID())};
        SQLiteDatabase st=getReadableDatabase();
        Cursor rsCheckExist=st.rawQuery(sqlCheckExist,agrsCheckExist);

        if (rsCheckExist!=null && rsCheckExist.moveToNext()) {
            //Nếu exits -> update
            String sql="update TestResultDetail set answerNumber = ? where testID = ? and questionID = ?";
            String[] agrs={String.valueOf(testResultDetail.getAnswerNumber()), String.valueOf(testResultDetail.getTestID()), String.valueOf(testResultDetail.getQuestionID())};
            SQLiteDatabase stUpdate = getWritableDatabase();
            stUpdate.execSQL(sql,agrs);
        } else {
            //Nếu k exits -> insert
            String sql="insert into TestResultDetail (testID, questionID, answerNumber, correctNumber) values (?, ?, ?, ?)";
            String[] agrs={String.valueOf(testResultDetail.getTestID()), String.valueOf(testResultDetail.getQuestionID()), String.valueOf(testResultDetail.getAnswerNumber()), String.valueOf(testResultDetail.getCorrectNumber())};
            SQLiteDatabase stInsert = getWritableDatabase();
            stInsert.execSQL(sql,agrs);
        }
        rsCheckExist.close();
        return;
    }

    public List<TestResultDetail> getTestResultDetail(int testID){
        List<TestResultDetail> testResultDetails = new ArrayList<>();
        String sql="select * from TestResultDetail where testID = ?";
        String[] args = {String.valueOf(testID)};
        SQLiteDatabase st=getReadableDatabase();
        Cursor rs=st.rawQuery(sql,args);

        while(rs!=null && rs.moveToNext()){
            int questionID = rs.getInt(1);
            int answerNumber = rs.getInt(2);
            int correctNumber = rs.getInt(3);
            TestResultDetail testResultDetail = new TestResultDetail(testID, questionID, answerNumber, correctNumber);

            String sqlQuestion = "select * from Question where id = ?";
            String[] argsQuestion = {String.valueOf(questionID)};
            Cursor rsQuestion = st.rawQuery(sqlQuestion,argsQuestion);

            if(rsQuestion!=null && rsQuestion.moveToNext()){
                int questionType_id = rsQuestion.getInt(1);
                String questionText  = rsQuestion.getString(2);
                String questionImageName  = rsQuestion.getString(3);
                String answer_1  = rsQuestion.getString(4);
                String answer_2  = rsQuestion.getString(5);
                String answer_3  = rsQuestion.getString(6);
                String answer_4  = rsQuestion.getString(7);
                int answer_correct = rsQuestion.getInt(8);
                String explanation = rsQuestion.getString(9);
                int question_die = rsQuestion.getInt(10); // 0:False - 1:True
                int practice_answer = rsQuestion.getInt(11);

                Question question = new Question(questionID,questionType_id,questionText,questionImageName,answer_1,answer_2,answer_3,answer_4,answer_correct,explanation,question_die,practice_answer);
                testResultDetail.setQuestion(question);
            }

            testResultDetails.add(testResultDetail);
        }
        rs.close();
        return testResultDetails;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDatabase() {
        boolean dbExist = checkDataBase();
        if(!dbExist){
            try {
                copyDataBase();
            } catch (IOException e) {
                System.out.println("Error copying database");
            }
        }
    }

    //Check database already exist or not
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            String myPath = DATABASE_PATH;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        } catch(SQLiteException e) {
            System.out.println("Error checkDatabase");
        }
        return checkDB;
    }

    private void copyDataBase() throws IOException{
        String outFileName = DATABASE_PATH;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }




}
