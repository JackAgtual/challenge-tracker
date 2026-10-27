"use client";

import { deleteGoal } from "@/actions/goal-definition";
import { toastProblemDetail } from "@/lib/toast-utils";
import { Button } from "@base-ui/react";
import { Trash } from "lucide-react";

type Props = {
  challengeId: number;
  goalDefinitionId: number;
};

export function DeleteGoalDefinition({ challengeId, goalDefinitionId }: Props) {
  async function handleDelete() {
    const { success, error } = await deleteGoal(challengeId, goalDefinitionId);

    if (success) {
      return;
    }

    toastProblemDetail(error);
  }

  return (
    <Button onClick={handleDelete}>
      <Trash />
    </Button>
  );
}
