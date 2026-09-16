"use client";

import { handleGoalUncompletion } from "@/actions/goal-completion";
import { CircleCheck } from "lucide-react";

type GoalCompletionIconProps = {
  challengeId: number;
  goalDefinitionId: number;
  goalCompletionId: number;
  challengeInProgress: boolean;
};

export default function GoalCompletionIcon({
  challengeId,
  goalDefinitionId,
  goalCompletionId,
  challengeInProgress,
}: GoalCompletionIconProps) {
  if (!challengeInProgress) {
    return <CircleCheck />;
  }

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
