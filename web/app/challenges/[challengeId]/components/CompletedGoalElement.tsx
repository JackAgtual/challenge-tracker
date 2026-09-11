"use client";

import { handleGoalUncompletion } from "@/actions/goal-completion";
import { CircleCheck } from "lucide-react";

type GoalCompletionIconProps = {
  challengeId: number;
  goalDefinitionId: number;
  goalCompletionId: number;
};

export default function GoalCompletionIcon({
  challengeId,
  goalDefinitionId,
  goalCompletionId,
}: GoalCompletionIconProps) {
  return (
    <CircleCheck
      className="cursor-pointer"
      onClick={() =>
        handleGoalUncompletion({
          challengeId,
          goalCompletionId,
          goalDefinitionId,
        })
      }
    />
  );
}
