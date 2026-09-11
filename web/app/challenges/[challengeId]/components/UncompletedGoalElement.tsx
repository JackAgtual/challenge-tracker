"use client";

import { handleGoalCompletion } from "@/actions/goal-completion";
import { Circle } from "lucide-react";

type UncompletedGoalElementProps = {
  challengeId: number;
  goalDefinitionId: number;
  date: string;
};

export default function UncompletedGoalElement({
  challengeId,
  goalDefinitionId,
  date,
}: UncompletedGoalElementProps) {
  return (
    <Circle
      className="cursor-pointer"
      onClick={() =>
        handleGoalCompletion({
          challengeId,
          goalDefinitionId,
          date,
        })
      }
    />
  );
}
