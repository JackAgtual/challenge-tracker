"use client";

import { handleGoalCompletion } from "@/actions/goal-completion";
import { toastGenericError } from "@/lib/toast-utils";
import { Circle } from "lucide-react";

type UncompletedGoalElementProps = {
  challengeId: number;
  goalDefinitionId: number;
  date: string;
  canModifyGoalCompletions: boolean;
};

export default function UncompletedGoalElement({
  challengeId,
  goalDefinitionId,
  date,
  canModifyGoalCompletions,
}: UncompletedGoalElementProps) {
  if (!canModifyGoalCompletions) {
    return <Circle />;
  }

  async function handleClick() {
    const { success } = await handleGoalCompletion({
      challengeId,
      goalDefinitionId,
      date,
    });

    if (!success) {
      toastGenericError();
    }
  }

  return <Circle className="cursor-pointer" onClick={handleClick} />;
}
