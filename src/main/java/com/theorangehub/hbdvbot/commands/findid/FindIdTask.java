package com.theorangehub.hbdvbot.commands.findid;

import com.theorangehub.hbdvbot.data.db.ManagedDatabase;
import gnu.trove.map.TObjectIntMap;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

@Data
@Builder
@Accessors(fluent = true)
public class FindIdTask implements Callable<FindIdTaskResult> {
    private final TObjectIntMap<String> atribsExatos;
    private final Set<String> atribsNegativos;
    private final Set<String> atribsPositivos;
    private final Map<String, AtribGen> atributos;

    public FindIdTaskResult call() throws InterruptedException {
        FindIdTaskResult result = new FindIdTaskResult();
        long id, i = -1;

        main:
        while (true) {
            i++;
            if (Thread.interrupted()) throw new InterruptedException();

            id = ManagedDatabase.ID_WORKER.generate();
            result.id(id).attempt(i).atributos().clear();

            Random rnd = new Random(id);
            
            for (Entry<String, AtribGen> entry : atributos.entrySet()) {
                String k = entry.getKey();
                AtribGen v = entry.getValue();

                int num = v.gen(rnd);
                result.atributos().put(k, num);

                if (atribsExatos.containsKey(k) && atribsExatos.get(k) != num) {
                    continue main;
                }

                if (atribsPositivos.contains(k) && !v.checkGood(num)) {
                    continue main;
                }

                if (atribsNegativos.contains(k) && !v.checkBad(num)) {
                    continue main;
                }
            }

            break;
        }

        return result;
    }
}
