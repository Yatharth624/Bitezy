package com.bitezy.app.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDao_Impl implements AppDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserGoalEntity> __insertionAdapterOfUserGoalEntity;

  private final EntityInsertionAdapter<FoodLogEntity> __insertionAdapterOfFoodLogEntity;

  public AppDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserGoalEntity = new EntityInsertionAdapter<UserGoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_goals` (`id`,`name`,`goal`,`targetCalories`,`targetProtein`,`targetWater`,`targetSteps`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserGoalEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getGoal());
        statement.bindLong(4, entity.getTargetCalories());
        statement.bindLong(5, entity.getTargetProtein());
        statement.bindLong(6, entity.getTargetWater());
        statement.bindLong(7, entity.getTargetSteps());
      }
    };
    this.__insertionAdapterOfFoodLogEntity = new EntityInsertionAdapter<FoodLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `food_logs` (`id`,`mealName`,`calories`,`protein`,`carbs`,`fat`,`foodScore`,`tip`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FoodLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getMealName());
        statement.bindLong(3, entity.getCalories());
        statement.bindLong(4, entity.getProtein());
        statement.bindLong(5, entity.getCarbs());
        statement.bindLong(6, entity.getFat());
        statement.bindDouble(7, entity.getFoodScore());
        statement.bindString(8, entity.getTip());
        statement.bindLong(9, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object saveUserGoal(final UserGoalEntity goal,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserGoalEntity.insert(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertFoodLog(final FoodLogEntity log,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFoodLogEntity.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserGoalEntity> getUserGoal() {
    final String _sql = "SELECT * FROM user_goals WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_goals"}, new Callable<UserGoalEntity>() {
      @Override
      @Nullable
      public UserGoalEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "goal");
          final int _cursorIndexOfTargetCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "targetCalories");
          final int _cursorIndexOfTargetProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "targetProtein");
          final int _cursorIndexOfTargetWater = CursorUtil.getColumnIndexOrThrow(_cursor, "targetWater");
          final int _cursorIndexOfTargetSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "targetSteps");
          final UserGoalEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpGoal;
            _tmpGoal = _cursor.getString(_cursorIndexOfGoal);
            final int _tmpTargetCalories;
            _tmpTargetCalories = _cursor.getInt(_cursorIndexOfTargetCalories);
            final int _tmpTargetProtein;
            _tmpTargetProtein = _cursor.getInt(_cursorIndexOfTargetProtein);
            final int _tmpTargetWater;
            _tmpTargetWater = _cursor.getInt(_cursorIndexOfTargetWater);
            final int _tmpTargetSteps;
            _tmpTargetSteps = _cursor.getInt(_cursorIndexOfTargetSteps);
            _result = new UserGoalEntity(_tmpId,_tmpName,_tmpGoal,_tmpTargetCalories,_tmpTargetProtein,_tmpTargetWater,_tmpTargetSteps);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<FoodLogEntity>> getAllFoodLogs() {
    final String _sql = "SELECT * FROM food_logs ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"food_logs"}, new Callable<List<FoodLogEntity>>() {
      @Override
      @NonNull
      public List<FoodLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMealName = CursorUtil.getColumnIndexOrThrow(_cursor, "mealName");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
          final int _cursorIndexOfCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "carbs");
          final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
          final int _cursorIndexOfFoodScore = CursorUtil.getColumnIndexOrThrow(_cursor, "foodScore");
          final int _cursorIndexOfTip = CursorUtil.getColumnIndexOrThrow(_cursor, "tip");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<FoodLogEntity> _result = new ArrayList<FoodLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodLogEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpMealName;
            _tmpMealName = _cursor.getString(_cursorIndexOfMealName);
            final int _tmpCalories;
            _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            final int _tmpProtein;
            _tmpProtein = _cursor.getInt(_cursorIndexOfProtein);
            final int _tmpCarbs;
            _tmpCarbs = _cursor.getInt(_cursorIndexOfCarbs);
            final int _tmpFat;
            _tmpFat = _cursor.getInt(_cursorIndexOfFat);
            final float _tmpFoodScore;
            _tmpFoodScore = _cursor.getFloat(_cursorIndexOfFoodScore);
            final String _tmpTip;
            _tmpTip = _cursor.getString(_cursorIndexOfTip);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new FoodLogEntity(_tmpId,_tmpMealName,_tmpCalories,_tmpProtein,_tmpCarbs,_tmpFat,_tmpFoodScore,_tmpTip,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
