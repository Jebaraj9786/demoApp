package org.example.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class WarService {

    private final Map<String, List<String>> advantages = Map.of(
            "Militia", List.of("Spearmen", "LightCavalry"),
            "Spearmen", List.of("LightCavalry", "HeavyCavalry"),
            "LightCavalry", List.of("FootArcher", "CavalryArcher"),
            "HeavyCavalry", List.of("Militia", "FootArcher", "LightCavalry"),
            "CavalryArcher", List.of("Spearmen", "HeavyCavalry"),
            "FootArcher", List.of("Militia", "CavalryArcher")
    );


    public String findWinningArrangement(String ownStr, String oppStr) {
        List<Platoon> ownList = parseInput(ownStr);
        List<Platoon> oppList = parseInput(oppStr);

        if (ownList.size() != oppList.size()) {
            return "Both own and opponent platoons must be of same size.";
        }

        int totalBattles = ownList.size();
        List<List<Platoon>> permutations = generatePermutations(ownList);

        for (List<Platoon> perm : permutations) {
            int wins = 0;
            for (int i = 0; i < totalBattles; i++) {
                Platoon own = perm.get(i);
                Platoon opp = oppList.get(i);
                if (canWin(own, opp)) {
                    wins++;
                }
            }

            // Determine majority (e.g., 2 of 3, 3 of 5)
            if (wins > totalBattles / 2) {
                return formatOutput(perm);
            }
        }

        return "Loss";
    }


    private boolean canWin(Platoon own, Platoon opp) {
        double ownStrength = own.count;
        if (advantages.getOrDefault(own.type, List.of()).contains(opp.type)) {
            ownStrength *= 2;
        }

        if (ownStrength > opp.count) return true;
        return false;
    }

    private List<Platoon> parseInput(String input) {
        List<Platoon> list = new ArrayList<>();
        for (String part : input.split(";")) {
            String[] split = part.split("#");
            list.add(new Platoon(split[0], Integer.parseInt(split[1])));
        }
        return list;
    }

    private String formatOutput(List<Platoon> list) {
        StringBuilder sb = new StringBuilder();
        for (Platoon p : list) {
            sb.append(p.type).append("#").append(p.count).append(";");
        }

        return "win - " + sb.substring(0, sb.length() - 1);
    }

    private List<List<Platoon>> generatePermutations(List<Platoon> input) {
        List<List<Platoon>> results = new ArrayList<>();
        permute(input, 0, results);
        return results;
    }

    private void permute(List<Platoon> arr, int index, List<List<Platoon>> results) {
        if (index == arr.size() - 1) {
            results.add(new ArrayList<>(arr));
            return;
        }
        for (int i = index; i < arr.size(); i++) {
            Collections.swap(arr, index, i);
            permute(arr, index + 1, results);
            Collections.swap(arr, index, i);
        }
    }

    private static class Platoon {
        String type;
        int count;

        Platoon(String type, int count) {
            this.type = type;
            this.count = count;
        }
    }
}

