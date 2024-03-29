#include <jni.h>
#include <string>
#include <cmath>
#include <algorithm>

class Planet {
public:
    float x;
    float y;
    float xs;
    float ys;
};

extern "C"
JNIEXPORT void JNICALL
Java_exdo_planets_Activity_12d_00024DrawView_recalculation(JNIEnv *env, jobject thiz,
                                                           jobjectArray planets, jshort length) {
    jclass obc = env->GetObjectClass(env->GetObjectArrayElement(planets, 0));

    jfieldID X = env->GetFieldID(obc, "x", "F");
    jfieldID Y = env->GetFieldID(obc, "y", "F");
    jfieldID XSpeed = env->GetFieldID(obc, "xspeed", "F");
    jfieldID YSpeed = env->GetFieldID(obc, "yspeed", "F");
    jfieldID Weight = env->GetFieldID(obc, "weight", "F");

    Planet np[length];
    float xa, ya, r, x, y, x2, y2, weight;

    for (short i = 0; i < length; i++) {
        xa = 0;
        ya = 0;
        x = env->GetFloatField(env->GetObjectArrayElement(planets, i), X);
        y = env->GetFloatField(env->GetObjectArrayElement(planets, i), Y);

        for (short j = 0; j < length; j++)
            if (i != j) {
                x2 = env->GetFloatField(env->GetObjectArrayElement(planets, j), X);
                y2 = env->GetFloatField(env->GetObjectArrayElement(planets, j), Y);
                weight = env->GetFloatField(env->GetObjectArrayElement(planets, j), Weight);

                r = sqrtf(powf(x2 - x, 2) + powf(y2 - y, 2));
                xa += weight * (x2 - x) / powf(r, 3);
                ya += weight * (y2 - y) / powf(r, 3);
            }

        np[i].xs = xa + env->GetFloatField(env->GetObjectArrayElement(planets, i), XSpeed);
        np[i].ys = ya + env->GetFloatField(env->GetObjectArrayElement(planets, i), YSpeed);

        np[i].x = x + np[i].xs;
        np[i].y = y + np[i].ys;

    }


    for (int i = 0; i < length; i++) {
        jobject ob = env->GetObjectArrayElement(planets, i);
        env->SetFloatField(ob, X, np[i].x);
        env->SetFloatField(ob, Y, np[i].y);
        env->SetFloatField(ob, XSpeed, np[i].xs);
        env->SetFloatField(ob, YSpeed, np[i].ys);


//        for (int j=planets[i].steps;j>0;j--)
//        {
//            planets[i].line[j][0]=planets[i].line[j-1][0];
//            planets[i].line[j][1]=planets[i].line[j-1][1];
//        }
//        planets[i].line[0][0]=planets[i].x;
//        planets[i].line[0][1]=planets[i].y;
//
//        if(planets[i].steps<999)planets[i].steps++;
    }
}