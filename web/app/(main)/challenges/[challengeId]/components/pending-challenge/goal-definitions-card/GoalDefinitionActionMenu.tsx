"use client";

import { deleteGoal } from "@/actions/goal-definition";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { toastProblemDetail } from "@/lib/toast-utils";
import { MoreHorizontalIcon } from "lucide-react";

type Props = {
  challengeId: number;
  goalDefinitionId: number;
};

export function GoalDefinitionActionMenu({
  challengeId,
  goalDefinitionId,
}: Props) {
  async function handleDelete() {
    const { success, error } = await deleteGoal(challengeId, goalDefinitionId);

    if (success) {
      return;
    }

    toastProblemDetail(error);
  }

  return (
    <DropdownMenu>
      <DropdownMenuTrigger
        nativeButton={false}
        render={<MoreHorizontalIcon />}
      />
      <DropdownMenuContent>
        <DropdownMenuGroup>
          <DropdownMenuItem variant="destructive" onClick={handleDelete}>
            Delete
          </DropdownMenuItem>
        </DropdownMenuGroup>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
